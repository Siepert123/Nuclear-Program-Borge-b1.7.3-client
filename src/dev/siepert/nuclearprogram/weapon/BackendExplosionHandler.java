package dev.siepert.nuclearprogram.weapon;

import dev.siepert.nuclearprogram.util.math.Vec3F;
import dev.siepert.nuclearprogram.world.entity.EntityExplosionHelper;
import net.minecraft.src.*;

import java.util.*;

// property of bob
public class BackendExplosionHandler {
	public static int shockwaveTicks = 0;

	public HashMap<ChunkCoordIntPair, List<Vec3F>> perChunk = new HashMap<>();
	public List<ChunkCoordIntPair> orderedChunks = new ArrayList<>();
	private final CoordComparator comparator = new CoordComparator();
	private final int posX, posY, posZ;
	public World worldObj;

	private final float strength;
	private final int length;
	private final int speed;
	private final int gspNumMax;
	private int gspNum;
	private double gspX, gspY;

	public boolean collectionComplete = false;

	public void writeNBT(NBTTagCompound nbt) {
		if (!this.perChunk.isEmpty()) {
			NBTTagList list = new NBTTagList(this.perChunk.size());

			for (Map.Entry<ChunkCoordIntPair, List<Vec3F>> entry : this.perChunk.entrySet()) {
				NBTTagCompound tag = new NBTTagCompound();

				tag.setInteger("cX", entry.getKey().chunkXPos);
				tag.setInteger("cZ", entry.getKey().chunkZPos);

				NBTTagList floats = new NBTTagList();
				for (Vec3F vec : entry.getValue()) {
					floats.setTag(new NBTTagIntArray(
							new int[]{
									Float.floatToRawIntBits(vec.x),
									Float.floatToRawIntBits(vec.y),
									Float.floatToRawIntBits(vec.z),
							}
					));
				}
				tag.setTag("Vectors", floats);

				list.setTag(tag);
			}

			nbt.setTag("PerChunk", list);
		}

		if (!this.orderedChunks.isEmpty()) {
			long[] packed = new long[this.orderedChunks.size()];
			for (int i = 0; i < this.orderedChunks.size(); i++) {
				ChunkCoordIntPair cp = this.orderedChunks.get(i);
				packed[i] = ((cp.chunkZPos & 0xFFFFFFFFL) << 32) | (cp.chunkXPos & 0xFFFFFFFFL);
			}
			nbt.setLongArray("OrderedChunks", packed);
		}

		nbt.setInteger("posX", this.posX);
		nbt.setInteger("posY", this.posY);
		nbt.setInteger("posZ", this.posZ);

		nbt.setFloat("strength", this.strength);
		nbt.setFloat("length", this.length);
		nbt.setInteger("gspNumMax", this.gspNumMax);
		nbt.setInteger("gspNum", this.gspNum);
		nbt.setDouble("gspX", this.gspX);
		nbt.setDouble("gspY", this.gspY);

		nbt.setBoolean("collectionComplete", this.collectionComplete);
	}

	public BackendExplosionHandler(NBTTagCompound nbt) {
		if (nbt.hasKey("PerChunk", NBTBase.LIST)) {
			NBTTagList list = nbt.getTagList("PerChunk");

			for (int i = 0; i < list.tagCount(); i++) {
				NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
				ChunkCoordIntPair cp = new ChunkCoordIntPair(compound.getInteger("cX"), compound.getInteger("cZ"));
				NBTTagList floats = compound.getTagList("Vectors");

				List<Vec3F> vec3Fs = new ArrayList<>(floats.tagCount());
				for (int j = 0; j < floats.tagCount(); j++) {
					int[] array = ((NBTTagIntArray)floats.tagAt(i)).arrayValue;
					vec3Fs.add(new Vec3F(
							Float.intBitsToFloat(array[0]),
							Float.intBitsToFloat(array[1]),
							Float.intBitsToFloat(array[2])
					));
				}
				this.perChunk.put(cp, vec3Fs);
			}
		}

		if (nbt.hasKey("OrderedChunks", NBTBase.LONG_ARRAY)) {
			long[] packed = nbt.getLongArray("OrderedChunks");
			for (long l : packed) {
				int cX = (int)l;
				int cZ = (int)(l >> 32);
				this.orderedChunks.add(new ChunkCoordIntPair(cX, cZ));
			}
		}

		this.posX = nbt.getInteger("posX");
		this.posY = nbt.getInteger("posY");
		this.posZ = nbt.getInteger("posZ");

		this.strength = nbt.getFloat("strength");
		this.speed = EntityExplosionHelper.EXPLOSION_CALCULATION_FACTOR;
		this.length = nbt.getInteger("length");
		this.gspNumMax = nbt.getInteger("gspNumMax");
		this.gspNum = nbt.getInteger("gspNum");
		this.gspX = nbt.getDouble("gspX");
		this.gspY = nbt.getDouble("gspY");

		this.collectionComplete = nbt.getBoolean("collectionComplete");
	}

	public BackendExplosionHandler(World worldObj, int x, int y, int z, float strength, int speed, int length) {
		this.worldObj = worldObj;
		this.posX = x;
		this.posY = y;
		this.posZ = z;
		this.strength = strength;
		this.speed = speed;
		this.length = length;

		this.gspNumMax = (int)(2.5 * Math.PI * Math.pow(this.strength, 2));
		this.gspNum = 1;

		this.gspX = Math.PI;
		this.gspY = 0.0;

		//WorldActiveExplosions.get(this.worldObj).setDirty(true);
	}

	private void generateGspUp() {
		if (this.gspNum < this.gspNumMax) {
			int k = this.gspNum + 1;
			double hk = -1.0 + 2.0 * (k - 1.0) / (this.gspNumMax - 1.0);
			this.gspX = Math.acos(hk);

			double prevLon = this.gspY;
			double lon = prevLon + 3.6 / Math.sqrt(this.gspNumMax) / Math.sqrt(1.0 - hk * hk);
			this.gspY = lon % (Math.PI * 2);
		} else {
			this.gspX = 0.0;
			this.gspY = 0.0;
		}
		this.gspNum++;
	}

	private Vec3D getSpherical2cartesian() {
		double dx = Math.sin(this.gspX) * Math.cos(this.gspY);
		double dz = Math.sin(this.gspX) * Math.sin(this.gspY);
		double dy = Math.cos(this.gspX);
		return Vec3D.createVector(dx, dy, dz);
	}

	public void collectTip(long limit) {
		WorldActiveExplosions.get(this.worldObj).setDirty(true);

		while (this.gspNumMax >= this.gspNum) {
			Vec3D vec = this.getSpherical2cartesian();

			float length = this.strength;
			float res = this.strength;

			Vec3F lastPos = null;
			HashSet<ChunkCoordIntPair> chunkCoords = new HashSet<>();

			for(int i = 0; i < length; i ++) {

				if(i > this.length)
					break;

				float x0 = (float) (this.posX + (vec.xCoord * i));
				float y0 = (float) (this.posY + (vec.yCoord * i));
				float z0 = (float) (this.posZ + (vec.zCoord * i));

				int iX = (int) Math.floor(x0);
				int iY = (int) Math.floor(y0);
				int iZ = (int) Math.floor(z0);

				double fac = 100 - ((double) i) / ((double) length) * 100;
				fac *= 0.07D;

				int blockID = this.worldObj.getBlockId(iX, iY, iZ);
				Block block = Block.blocksList[blockID];

				if(!(block instanceof BlockFluid))
					res -= (float) Math.pow(resistance(block), 7.5D - fac);

				if(res > 0 && block != null) {
					lastPos = new Vec3F(x0, y0, z0);
					//all-air chunks don't need to be buffered at all
					ChunkCoordIntPair chunkPos = new ChunkCoordIntPair(iX >> 4, iZ >> 4);
					chunkCoords.add(chunkPos);
				}

				if(res <= 0 || i + 1 >= this.length || i == length - 1) {
					break;
				}
			}

			for (ChunkCoordIntPair pos : chunkCoords) {
				List<Vec3F> triplets = this.perChunk.computeIfAbsent(pos, k -> new ArrayList<>());
				triplets.add(lastPos);
			}

			this.generateGspUp();

			if (System.currentTimeMillis() >= limit) return;
		}

		this.orderedChunks.addAll(this.perChunk.keySet());
		this.orderedChunks.sort(this.comparator);

		this.collectionComplete = true;
	}

	public static float resistance(Block block) {
		if (block == null) return 0.0F;
		return block.getExplosionResistance(null);
	}

	public class CoordComparator implements Comparator<ChunkCoordIntPair> {
		@Override
		public int compare(ChunkCoordIntPair o1, ChunkCoordIntPair o2) {
			int x = BackendExplosionHandler.this.posX >> 4;
			int z = BackendExplosionHandler.this.posZ >> 4;

			int d1 = Math.abs((x - o1.chunkXPos)) + Math.abs((z - o1.chunkZPos));
			int d2 = Math.abs((x - o2.chunkXPos)) + Math.abs((z - o2.chunkZPos));

			return d1 - d2;
		}
	}

	public void processChunk() {
		if (this.perChunk.isEmpty()) return;
		WorldActiveExplosions.get(this.worldObj).setDirty(true);

		ChunkCoordIntPair cp = this.orderedChunks.get(0);
		List<Vec3F> list = this.perChunk.get(cp);

		this.worldObj.editingBlocks = true;

		int chunkX = cp.chunkXPos;
		int chunkZ = cp.chunkZPos;

		int enter = Math.min(
				Math.abs(this.posX - (chunkX << 4)),
				Math.abs(this.posZ - (chunkZ << 4))
		) - 16;
		enter = Math.max(enter, 0);

		for (Vec3F vec3f : list) {
			Vec3D vec = Vec3D.createVector(vec3f.x - this.posX, vec3f.y - this.posY, vec3f.z - this.posZ);
			double pX = vec.xCoord / vec.lengthVector();
			double pY = vec.yCoord / vec.lengthVector();
			double pZ = vec.zCoord / vec.lengthVector();

			int tipX = (int) Math.floor(vec3f.x);
			int tipY = (int) Math.floor(vec3f.y);
			int tipZ = (int) Math.floor(vec3f.z);

			boolean inChunk = false;
			for (int i = enter; i < vec.lengthVector(); i++) {
				int x = MathHelper.floor_double(this.posX + pX * i);
				int y = MathHelper.floor_double(this.posY + pY * i);
				int z = MathHelper.floor_double(this.posZ + pZ * i);

				if (x >> 4 != chunkX || z >> 4 != chunkZ) {
					if (inChunk) {
						break;
					} else {
						continue;
					}
				}

				inChunk = true;

				this.worldObj.setBlockWithNotify(x, y, z, 0);
			}
		}

		this.perChunk.remove(cp);
		this.orderedChunks.remove(0);

		this.worldObj.editingBlocks = false;
	}

	public boolean isComplete() {
		if (this.collectionComplete && this.perChunk.isEmpty()) {
			System.out.println("done!");
			List<EntityExplosionHelper> candidates = this.worldObj.getEntitiesWithinAABB(EntityExplosionHelper.class, AxisAlignedBB.getBoundingBoxFromPool(
					this.posX-1, this.posY-1, this.posZ-1, this.posX+2, this.posY+2, this.posZ+2
			));
			candidates.forEach(ExploderParent::notifyExplosionFinished);
			return true;
		}
		return false;
	}

	public void cacheChunksTick(int msBudget) {
		if (!this.collectionComplete) {
			this.collectTip(System.currentTimeMillis() + msBudget);
		}
	}

	public void destructionTick(int msBudget) {
		if (!this.collectionComplete) return;
		long start = System.currentTimeMillis();
		while (!this.perChunk.isEmpty() && System.currentTimeMillis() < start + msBudget) this.processChunk();
	}

	public void cancel() {
		this.collectionComplete = true;
		if (this.perChunk != null) this.perChunk.clear();
		if (this.orderedChunks != null) this.orderedChunks.clear();
	}
}
