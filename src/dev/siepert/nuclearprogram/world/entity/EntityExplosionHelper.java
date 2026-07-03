package dev.siepert.nuclearprogram.world.entity;

import dev.siepert.nuclearprogram.init.BlockInit;
import dev.siepert.nuclearprogram.weapon.*;
import net.minecraft.src.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class EntityExplosionHelper extends Entity implements ExploderParent {
	public static final int EXPLOSION_CALCULATION_FACTOR = 500;
	public static final boolean ENABLE_ENTITY_DAMAGE = true;
	public static final boolean ENABLE_CHUNK_PREGENERATION = true;
	public static final int CHUNK_PREGENERATION_BUDGET = 20;
	public static final boolean ENABLE_WATER_REFILL = false;

	public EntityExplosionHelper(World world) {
		super(world);
	}

	public EntityExplosionHelper(World world, double x, double y, double z, NukeType type) {
		this(world);
		this.setPosition(x, y, z);
		this.nukeType = type;
		this.evaporationMin = (int) Math.max(this.posY - this.nukeType.getBlastRadius(), 0);
		this.evaporationMax = (int) Math.min(this.posY + this.nukeType.getBlastRadius(), 128);

		int r = this.nukeType.getEntityBlowRadius();
		this.entitySearch = AxisAlignedBB.getBoundingBox(
				this.posX - r, this.posY - r, this.posZ - r,
				this.posX + r, this.posY + r, this.posZ + r
		);

		System.out.println("New explosion helper entity");
		this.start = System.currentTimeMillis();
	}

	@Override
	protected void entityInit() {

	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		this.nukeType = NukeTypes.LIST[nbt.getByte("nukeType") & 255];

		this.start = System.currentTimeMillis() - nbt.getLong("start");

		this.affectedMinCX = nbt.getInteger("affectMinCX");
		this.affectedMaxCX = nbt.getInteger("affectMaxCX");
		this.affectedMinCZ = nbt.getInteger("affectMinCZ");
		this.affectedMaxCZ = nbt.getInteger("affectMaxCZ");

		this.evaporationMin = nbt.getInteger("evaporationMin");
		this.evaporationMax = nbt.getInteger("evaporationMax");

		this.chunksPregenerated = nbt.getBoolean("chunksPregenerated");
		this.hasSpawnedCloud = nbt.getBoolean("hasSpawnedCloud");
		this.waterEvaporated = nbt.getBoolean("waterEvaporated");
		this.explosionStarted = nbt.getBoolean("explosionStarted");
		this.explosionFinished = nbt.getBoolean("explosionFinished");
		this.leavesDestroyed = nbt.getBoolean("leavesDestroyed");
		this.treesCharred = nbt.getBoolean("treesCharred");
		this.radiationReleased = nbt.getBoolean("radiationReleased");
		this.biomeConverted = nbt.getBoolean("biomeConverted");
		this.nuclearRemainsPlaced = nbt.getBoolean("nuclearRemainsPlaced");
		this.waterRefilled = nbt.getBoolean("waterRefilled");
		this.waterRefilled2 = nbt.getBoolean("waterRefilled2");
		this.waterRefilled3 = nbt.getBoolean("waterRefilled3");
		this.waterRefilled4 = nbt.getBoolean("waterRefilled4");

		this.age = nbt.getInteger("age");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setByte("nukeType", (byte) this.nukeType.registryID);

		nbt.setLong("start", System.currentTimeMillis() - this.start);

		nbt.setInteger("affectedMinCX", this.affectedMinCX);
		nbt.setInteger("affectedMaxCX", this.affectedMaxCX);
		nbt.setInteger("affectedMinCZ", this.affectedMinCZ);
		nbt.setInteger("affectedMaxCZ", this.affectedMaxCZ);

		nbt.setInteger("evaporationMin", this.evaporationMin);
		nbt.setInteger("evaporationMax", this.evaporationMax);

		nbt.setBoolean("chunksPregenerated", this.chunksPregenerated);
		nbt.setBoolean("hasSpawnedCloud", this.hasSpawnedCloud);
		nbt.setBoolean("waterEvaporated", this.waterEvaporated);
		nbt.setBoolean("explosionStarted", this.explosionStarted);
		nbt.setBoolean("explosionFinished", this.explosionFinished);
		nbt.setBoolean("leavesDestroyed", this.leavesDestroyed);
		nbt.setBoolean("treesCharred", this.treesCharred);
		nbt.setBoolean("radiationReleased", this.radiationReleased);
		nbt.setBoolean("biomeConverted", this.biomeConverted);
		nbt.setBoolean("nuclearRemainsPlaced", this.nuclearRemainsPlaced);
		nbt.setBoolean("waterRefilled", this.waterRefilled);
		nbt.setBoolean("waterRefilled2", this.waterRefilled2);
		nbt.setBoolean("waterRefilled3", this.waterRefilled3);
		nbt.setBoolean("waterRefilled4", this.waterRefilled4);

		nbt.setInteger("age", this.age);
	}

	public NukeType nukeType;

	private long start;
	private int affectedMinCX, affectedMinCZ;
	private int affectedMaxCX, affectedMaxCZ;
	private int evaporationMin, evaporationMax;
	private List<ChunkCoordIntPair> orderedChunks = null;

	private boolean chunksPregenerated = !ENABLE_CHUNK_PREGENERATION;
	private boolean hasSpawnedCloud = false;
	private boolean waterEvaporated = false;
	private boolean explosionStarted = false;
	private boolean explosionFinished = false;
	private boolean leavesDestroyed = false;
	private boolean treesCharred = false;
	private boolean radiationReleased = false;
	private boolean biomeConverted = false;
	private boolean nuclearRemainsPlaced = false;
	private boolean waterRefilled = false;
	private boolean waterRefilled2 = false;
	private boolean waterRefilled3 = false;
	private boolean waterRefilled4 = false;

	private AxisAlignedBB entitySearch = null;

	public int age = -1;

	@Override
	public void onUpdate() {
		this.age++;
		if (this.nukeType == null) return;
		if (this.nukeType.brightensSky() && this.age <= this.nukeType.getMushroomCloudSize() * 25.0F) this.worldObj.skyFlashes = 20;
		if (this.worldObj.multiplayerWorld) return;

		if (ENABLE_ENTITY_DAMAGE && (this.age & 31) == 0 && !(this.waterRefilled || this.waterRefilled2 || this.waterRefilled3 || this.waterRefilled4)) {
			try {
				int r = this.nukeType.getEntityBlowRadius();
				int r2 = r * r;
				List<Entity> entities = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.entitySearch);
				for (Entity entity : entities) {
					double d2 = entity.getDistanceSqToEntity(this);
					if (d2 > r2) continue;
					double d = Math.sqrt(d2);
					double inv = r - d;
					if (!this.worldObj.canBlockSeeTheSky((int) entity.posX, (int) entity.posY, (int) entity.posZ))
						continue;
					double dx = entity.posX - this.posX;
					double dy = entity.posY - this.posY;
					double dz = entity.posZ - this.posZ;
					entity.motionX += dx * 0.01;
					entity.motionY += dy * 0.01;
					entity.motionZ += dz * 0.01;
					entity.fire = (int) Math.ceil(inv * this.nukeType.getEntityFireTicks());
					entity.attackEntityFrom(this, (int) (inv * this.nukeType.entityDamageMultiplier()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (!this.hasSpawnedCloud) {
			System.out.println("Starting cloud");
			this.worldObj.playEvent(this.nukeType.getEventID(), (int)this.posX, (int)this.posY, (int)this.posZ, this.nukeType.getEventData());
			this.hasSpawnedCloud = true;
			return;
		}
		if (!this.chunksPregenerated) {
			if (this.orderedChunks == null) {
				System.out.println("Starting chunk pregeneration");
				int c = (this.nukeType.getAffectedRange() + 15) >> 4;
				this.affectedMinCX = this.chunkCoordX - c;
				this.affectedMaxCX = this.chunkCoordX + c;
				this.affectedMinCZ = this.chunkCoordZ - c;
				this.affectedMaxCZ = this.chunkCoordZ + c;
				this.orderedChunks = new ArrayList<>((this.affectedMaxCX-this.affectedMinCX)*(this.affectedMaxCZ-this.affectedMinCZ));
				for (int x = this.affectedMinCX; x <= this.affectedMaxCX; ++x) {
					for (int z = this.affectedMinCZ; z <= this.affectedMaxCZ; ++z) {
						this.orderedChunks.add(new ChunkCoordIntPair(x, z));
					}
				}

				this.orderedChunks.sort((cp1, cp2) -> {
					int d1 = this.manhattanDistance(cp1);
					int d2 = this.manhattanDistance(cp2);

					return d1 - d2;
				});
				System.out.println("Chunk count to load: " + this.orderedChunks.size());
				return;
			}
			if (this.orderedChunks.isEmpty()) {
				this.orderedChunks = null;
				this.chunksPregenerated = true;
			} else {
				long limit = System.currentTimeMillis() + CHUNK_PREGENERATION_BUDGET;
				while ((System.currentTimeMillis() < limit) && !this.orderedChunks.isEmpty()) {
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					this.worldObj.getChunkFromChunkCoords(cp.chunkXPos, cp.chunkZPos);
				}
			}
			return;
		}
		if (!this.waterEvaporated) {
			if (this.orderedChunks == null) {
				System.out.println("Starting water evaporation");
				int c = (this.nukeType.getBlastRadius() + 15) >> 4;
				this.affectedMinCX = this.chunkCoordX - c;
				this.affectedMaxCX = this.chunkCoordX + c;
				this.affectedMinCZ = this.chunkCoordZ - c;
				this.affectedMaxCZ = this.chunkCoordZ + c;
				this.orderedChunks = new ArrayList<>((this.affectedMaxCX-this.affectedMinCX)*(this.affectedMaxCZ-this.affectedMinCZ));
				for (int x = this.affectedMinCX; x <= this.affectedMaxCX; ++x) {
					for (int z = this.affectedMinCZ; z <= this.affectedMaxCZ; ++z) {
						this.orderedChunks.add(new ChunkCoordIntPair(x, z));
					}
				}

				this.orderedChunks.sort((cp1, cp2) -> {
					int d1 = this.chessboardDistance(cp1);
					int d2 = this.chessboardDistance(cp2);

					return d1 - d2;
				});
				return;
			}
			if (this.orderedChunks.isEmpty()) {
				this.orderedChunks = null;
				this.waterEvaporated = true;
			} else {
				int max = Math.max(EXPLOSION_CALCULATION_FACTOR / 50, 1);
				this.worldObj.editingBlocks = true;
				for (int i = 0; (i < max && !this.orderedChunks.isEmpty()); i++) {
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					Material material;
					for (x = 0; x < 16; x++) {
						for (z = 0; z < 16; z++) {
							for (y = this.evaporationMin; y < this.evaporationMax; y++) {
								material = this.worldObj.getBlockMaterial((cp.chunkXPos << 4) + x, y, (cp.chunkZPos << 4) + z);
								if (material == Material.water || material == Material.snow || material == Material.builtSnow) {
									this.worldObj.setBlockWithNotify((cp.chunkXPos << 4) + x, y, (cp.chunkZPos << 4) + z, 0);
								}
							}
						}
					}
				}
				this.worldObj.editingBlocks = false;
			}
			return;
		}
		if (!this.explosionStarted) {
			System.out.println("Starting explosion");
			WorldActiveExplosions.get(this.worldObj).add(
					new BackendExplosionHandler(
							this.worldObj,
							(int) this.posX,
							(int) this.posY,
							(int) this.posZ,
							this.nukeType.getBlastStrength(),
							EXPLOSION_CALCULATION_FACTOR,
							this.nukeType.getBlastRadius()
					)
			);
			this.explosionStarted = true;
			return;
		}
		if (!this.explosionFinished) {
			return;
		}
		if (!this.radiationReleased) {
			this.radiationReleased = true;
			return;
		}
		if (!this.biomeConverted) {
			this.biomeConverted = true;
			return;
		}
		if (!this.leavesDestroyed) {
			if (this.orderedChunks == null) {
				System.out.println("Starting leave destruction");
				int c = (this.nukeType.getDestroyedLeavesRadius() + 15) >> 4;
				this.affectedMinCX = this.chunkCoordX - c;
				this.affectedMaxCX = this.chunkCoordX + c;
				this.affectedMinCZ = this.chunkCoordZ - c;
				this.affectedMaxCZ = this.chunkCoordZ + c;
				this.orderedChunks = new ArrayList<>((this.affectedMaxCX-this.affectedMinCX)*(this.affectedMaxCZ-this.affectedMinCZ));
				for (int x = this.affectedMinCX; x <= this.affectedMaxCX; ++x) {
					for (int z = this.affectedMinCZ; z <= this.affectedMaxCZ; ++z) {
						this.orderedChunks.add(new ChunkCoordIntPair(x, z));
					}
				}

				this.orderedChunks.sort((cp1, cp2) -> {
					int d1 = this.chessboardDistance(cp1);
					int d2 = this.chessboardDistance(cp2);

					return d1 - d2;
				});
				return;
			}
			if (this.orderedChunks.isEmpty()) {
				this.orderedChunks = null;
				this.leavesDestroyed = true;
			} else {
				int max = Math.max(EXPLOSION_CALCULATION_FACTOR / 100, 1);
				this.worldObj.editingBlocks = true;
				for (int i = 0; (i < max && !this.orderedChunks.isEmpty()); i++) {
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					int x1, z1, sqr;
					Material material;
					int d2 = this.nukeType.getDestroyedLeavesRadius() * this.nukeType.getDestroyedLeavesRadius();
					for (x = 0; x < 16; x++) {
						for (z = 0; z < 16; z++) {
							x1 = (x + (cp.chunkXPos << 4) - (int)this.posX);
							z1 = (z + (cp.chunkZPos << 4) - (int)this.posZ);
							sqr = x1 * x1 + z1 * z1;
							if (sqr > d2) continue;
							if (sqr == d2 && this.rand.nextBoolean()) continue;

							for (y = 64; y < 128; y++) {
								material = this.worldObj.getBlockMaterial((cp.chunkXPos << 4) + x, y, (cp.chunkZPos << 4) + z);
								if (material == Material.plants || material == Material.leaves) {
									this.worldObj.setBlockWithNotify((cp.chunkXPos << 4) + x, y, (cp.chunkZPos << 4) + z, 0);
								}
							}
						}
					}
				}
				this.worldObj.editingBlocks = false;
			}
			return;
		}
		if (!this.treesCharred) {
			if (this.orderedChunks == null) {
				if (this.nukeType.getCharredTreesRadius() <= 0) {
					this.treesCharred = true;
					return;
				}
				System.out.println("Starting charred trees");
				int c = (this.nukeType.getCharredTreesRadius() + 15) >> 4;
				this.affectedMinCX = this.chunkCoordX - c;
				this.affectedMaxCX = this.chunkCoordX + c;
				this.affectedMinCZ = this.chunkCoordZ - c;
				this.affectedMaxCZ = this.chunkCoordZ + c;
				this.orderedChunks = new ArrayList<>((this.affectedMaxCX-this.affectedMinCX)*(this.affectedMaxCZ-this.affectedMinCZ));
				for (int x = this.affectedMinCX; x <= this.affectedMaxCX; ++x) {
					for (int z = this.affectedMinCZ; z <= this.affectedMaxCZ; ++z) {
						this.orderedChunks.add(new ChunkCoordIntPair(x, z));
					}
				}

				this.orderedChunks.sort((cp1, cp2) -> {
					int d1 = this.chessboardDistance(cp1);
					int d2 = this.chessboardDistance(cp2);

					return d1 - d2;
				});
				return;
			}
			if (this.orderedChunks.isEmpty()) {
				this.orderedChunks = null;
				this.treesCharred = true;
			} else {
				this.worldObj.editingBlocks = true;
				int d = this.nukeType.getCharredTreesRadius();
				int d2 = d * d;
				int d3 = (d+16) * (d+16);
				int max = Math.max(EXPLOSION_CALCULATION_FACTOR / 100, 1);
				for (int i = 0; (i < max && !this.orderedChunks.isEmpty()); i++) {
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					int x2, z2, x1, z1;
					int sqr, blockID;
					Material material;
					for (x = 0; x < 16; x++) {
						for (z = 0; z < 16; z++) {
							x2 = x + (cp.chunkXPos << 4);
							z2 = z + (cp.chunkZPos << 4);
							x1 = x2 - (int)this.posX;
							z1 = z2 - (int)this.posZ;
							sqr = x1 * x1 + z1 * z1;
							if (sqr > d3) continue;
							for (y = 60; y < 128; y++) {
								if (sqr > d2 && this.rand.nextBoolean()) continue;
								blockID = this.worldObj.getBlockId(x2, y, z2);
								if (blockID > 0) {
									material = Block.blocksList[blockID].blockMaterial;
									if (material == Material.wood) {
										if (blockID == Block.wood.blockID) {
											this.worldObj.setBlockAndMetadataWithNotify(x2, y, z2, BlockInit.charredWood.blockID, 0);
										} else {
											this.worldObj.setBlockAndMetadataWithNotify(x2, y, z2, BlockInit.charredWood.blockID, 1);
										}
									} else if (blockID == Block.grass.blockID) {
										this.worldObj.setBlockWithNotify(x2, y, z2, Block.dirt.blockID);
									}
								}
							}
						}
					}
				}
				this.worldObj.editingBlocks = false;
			}
			return;
		}
		if (!this.nuclearRemainsPlaced) {
			if (this.orderedChunks == null) {
				if (this.nukeType.getNuclearRemainsRadius() <= 0) {
					this.nuclearRemainsPlaced = true;
					return;
				}
				System.out.println("Starting nuclear remains");
				int c = (this.nukeType.getNuclearRemainsRadius() + 15) >> 4;
				this.affectedMinCX = this.chunkCoordX - c;
				this.affectedMaxCX = this.chunkCoordX + c;
				this.affectedMinCZ = this.chunkCoordZ - c;
				this.affectedMaxCZ = this.chunkCoordZ + c;
				this.orderedChunks = new ArrayList<>((this.affectedMaxCX-this.affectedMinCX)*(this.affectedMaxCZ-this.affectedMinCZ));
				for (int x = this.affectedMinCX; x <= this.affectedMaxCX; ++x) {
					for (int z = this.affectedMinCZ; z <= this.affectedMaxCZ; ++z) {
						this.orderedChunks.add(new ChunkCoordIntPair(x, z));
					}
				}

				this.orderedChunks.sort((cp1, cp2) -> {
					int d1 = this.chessboardDistance(cp1);
					int d2 = this.chessboardDistance(cp2);

					return d1 - d2;
				});
				return;
			}
			if (this.orderedChunks.isEmpty()) {
				this.orderedChunks = null;
				this.nuclearRemainsPlaced = true;
			} else {
				this.worldObj.editingBlocks = true;
				float quart = this.nukeType.getNuclearRemainsRadius() * 0.25F;
				float quart2 = quart * 2.0F;
				float quart3 = quart * 3.0F;
				int max = Math.max(EXPLOSION_CALCULATION_FACTOR / 20, 1);
				for (int i = 0; (i < max && !this.orderedChunks.isEmpty()); i++) {
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z, x2, z2;
					int d = this.nukeType.getNuclearRemainsRadius();
					int d2 = d * d;
					int d3 = (d + 16) * (d + 16);
					int darkness;
					for (x = 0; x < 16; x++) {
						for (z = 0; z < 16; z++) {
							x2 = x + (cp.chunkXPos << 4);
							z2 = z + (cp.chunkZPos << 4);
							{
								int x1 = (x2 - (int)this.posX);
								int z1 = (z2 - (int)this.posZ);
								int sqr = x1 * x1 + z1 * z1;
								if (sqr > d3) continue;
								if (sqr > d2 && this.rand.nextBoolean()) continue;
								if (this.nukeType.hasDarkenedNuclearRemains()) {
									float dist = MathHelper.sqrt_float(sqr);
									if (dist < quart) {
										darkness = 3;
									} else if (dist < quart2) {
										darkness = 2;
									} else if (dist < quart3) {
										darkness = 1;
									} else darkness = 0;
								} else {
									darkness = 0;
								}
							}
							y = this.worldObj.getHeightValue(x2, z2) - 1;
							int blockID;

							blockID = this.worldObj.getBlockId(x2, y, z2);
							if (blockID == Block.oreCoal.blockID || blockID == Block.oreDiamond.blockID) {
								this.worldObj.setBlockWithNotify(x2, y, z2, Block.oreDiamond.blockID);
							} else if (this.validNuclearRemains(blockID, 250.0F)) {
								this.worldObj.setBlockAndMetadataWithNotify(x2, y, z2, BlockInit.nukestone.blockID, darkness);
							}
							y--;
							blockID = this.worldObj.getBlockId(x2, y, z2);
							if (blockID == Block.oreCoal.blockID || blockID == Block.oreDiamond.blockID) {
								this.worldObj.setBlockWithNotify(x2, y, z2, Block.oreDiamond.blockID);
							} else if (this.validNuclearRemains(blockID, 150.0F)) {
								this.worldObj.setBlockAndMetadataWithNotify(x2, y, z2, BlockInit.nukestone.blockID, darkness);
							}
							y--;
							blockID = this.worldObj.getBlockId(x2, y, z2);
							if (blockID == Block.oreCoal.blockID || blockID == Block.oreDiamond.blockID) {
								this.worldObj.setBlockWithNotify(x2, y, z2, Block.oreDiamond.blockID);
							} else if (this.validNuclearRemains(blockID, 50.0F)) {
								this.worldObj.setBlockAndMetadataWithNotify(x2, y, z2, BlockInit.nukestone.blockID, darkness);
							}
						}
					}
				}
				this.worldObj.editingBlocks = false;
			}
			return;
		}
		if (ENABLE_WATER_REFILL) {
			if (!this.waterRefilled) {
				if (this.orderedChunks == null) {
					System.out.println("Starting refill 1");
					this.waterRefilled2 = true;
					int c = (this.nukeType.getBlastRadius() + 15) >> 4;
					this.affectedMinCX = this.chunkCoordX - c;
					this.affectedMaxCX = this.chunkCoordX + c;
					this.affectedMinCZ = this.chunkCoordZ - c;
					this.affectedMaxCZ = this.chunkCoordZ + c;
					this.orderedChunks = new ArrayList<>();
					rangeClosed(new ChunkCoordIntPair(this.affectedMinCX, this.affectedMinCZ), new ChunkCoordIntPair(this.affectedMaxCX, this.affectedMaxCZ))
							.forEach(this.orderedChunks::add);
					return;
				}
				if (this.orderedChunks.isEmpty()) {
					this.orderedChunks = null;
					this.waterRefilled = true;
				} else {
					this.worldObj.editingBlocks = true;
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					Material material;
					for (x = 0; x < 16; x++) {
						for (z = 0; z < 16; z++) {
							for (y = this.evaporationMax - 1; y >= this.evaporationMin; y--) {
								material = this.worldObj.getBlockMaterial(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4));
								if (material.isReplaceable()) {
									if (checkWater(this.worldObj, x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4))) {
										this.waterRefilled2 = false;
										this.worldObj.setBlockWithNotify(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4), waterID);
									}
								}
							}
						}
					}
					this.worldObj.editingBlocks = false;
				}
				return;
			}
			if (!this.waterRefilled2) {
				if (this.orderedChunks == null) {
					System.out.println("Starting refill 2");
					this.waterRefilled3 = true;
					int c = (this.nukeType.getBlastRadius() + 15) >> 4;
					this.affectedMinCX = this.chunkCoordX - c;
					this.affectedMaxCX = this.chunkCoordX + c;
					this.affectedMinCZ = this.chunkCoordZ - c;
					this.affectedMaxCZ = this.chunkCoordZ + c;
					this.orderedChunks = new ArrayList<>();
					rangeClosed(new ChunkCoordIntPair(this.affectedMinCX, this.affectedMinCZ), new ChunkCoordIntPair(this.affectedMaxCX, this.affectedMaxCZ))
							.forEach(this.orderedChunks::add);
					return;
				}
				if (this.orderedChunks.isEmpty()) {
					this.orderedChunks = null;
					this.waterRefilled2 = true;
				} else {
					this.worldObj.editingBlocks = true;
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					Material material;
					for (x = 15; x >= 0; x--) {
						for (z = 0; z < 16; z++) {
							for (y = this.evaporationMax - 1; y >= this.evaporationMin; y--) {
								material = this.worldObj.getBlockMaterial(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4));
								if (material.isReplaceable()) {
									if (checkWater(this.worldObj, x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4))) {
										this.waterRefilled3 = false;
										this.worldObj.setBlockWithNotify(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4), waterID);
									}
								}
							}
						}
					}
					this.worldObj.editingBlocks = false;
				}
				return;
			}
			if (!this.waterRefilled3) {
				if (this.orderedChunks == null) {
					System.out.println("Starting refill 3");
					this.waterRefilled4 = true;
					int c = (this.nukeType.getBlastRadius() + 15) >> 4;
					this.affectedMinCX = this.chunkCoordX - c;
					this.affectedMaxCX = this.chunkCoordX + c;
					this.affectedMinCZ = this.chunkCoordZ - c;
					this.affectedMaxCZ = this.chunkCoordZ + c;
					this.orderedChunks = new ArrayList<>();
					rangeClosed(new ChunkCoordIntPair(this.affectedMinCX, this.affectedMinCZ), new ChunkCoordIntPair(this.affectedMaxCX, this.affectedMaxCZ))
							.forEach(this.orderedChunks::add);
					return;
				}
				if (this.orderedChunks.isEmpty()) {
					this.orderedChunks = null;
					this.waterRefilled3 = true;
				} else {
					this.worldObj.editingBlocks = true;
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					Material material;
					for (x = 0; x < 16; x++) {
						for (z = 15; z >= 0; z--) {
							for (y = this.evaporationMax - 1; y >= this.evaporationMin; y--) {
								material = this.worldObj.getBlockMaterial(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4));
								if (material.isReplaceable()) {
									if (checkWater(this.worldObj, x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4))) {
										this.waterRefilled4 = false;
										this.worldObj.setBlockWithNotify(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4), waterID);
									}
								}
							}
						}
					}
					this.worldObj.editingBlocks = false;
				}
				return;
			}
			if (!this.waterRefilled4) {
				if (this.orderedChunks == null) {
					System.out.println("Starting refill 4");
					int c = (this.nukeType.getBlastRadius() + 15) >> 4;
					this.affectedMinCX = this.chunkCoordX - c;
					this.affectedMaxCX = this.chunkCoordX + c;
					this.affectedMinCZ = this.chunkCoordZ - c;
					this.affectedMaxCZ = this.chunkCoordZ + c;
					this.orderedChunks = new ArrayList<>();
					rangeClosed(new ChunkCoordIntPair(this.affectedMinCX, this.affectedMinCZ), new ChunkCoordIntPair(this.affectedMaxCX, this.affectedMaxCZ))
							.forEach(this.orderedChunks::add);
					return;
				}
				if (this.orderedChunks.isEmpty()) {
					this.orderedChunks = null;
					this.waterRefilled4 = true;
				} else {
					this.worldObj.editingBlocks = true;
					ChunkCoordIntPair cp = this.orderedChunks.remove(0);
					int x, y, z;
					Material material;
					for (x = 15; x >= 0; x--) {
						for (z = 15; z >= 0; z--) {
							for (y = this.evaporationMax - 1; y >= this.evaporationMin; y--) {
								material = this.worldObj.getBlockMaterial(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4));
								if (material.isReplaceable()) {
									if (checkWater(this.worldObj, x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4))) {
										this.worldObj.setBlockWithNotify(x + (cp.chunkXPos << 4), y, z + (cp.chunkZPos << 4), waterID);
									}
								}
							}
						}
					}
					this.worldObj.editingBlocks = false;
				}
				return;
			}
		}
		System.out.println("Explosion finished in " + (System.currentTimeMillis() - this.start) + "ms");
		this.setEntityDead();
	}


	private int chessboardDistance(ChunkCoordIntPair cp) {
		return Math.max(Math.abs(this.chunkCoordX - cp.chunkXPos), Math.abs(this.chunkCoordZ - cp.chunkZPos));
	}
	private int manhattanDistance(ChunkCoordIntPair cp) {
		return Math.abs(this.chunkCoordX - cp.chunkXPos) + Math.abs(this.chunkCoordZ - cp.chunkZPos);
	}

	@Override
	public void notifyExplosionFinished() {
		System.out.println("Explosion finished");
		this.explosionFinished = true;
	}

	private boolean validNuclearRemains(int blockID, float resistance) {
		if (blockID == 0) return false;
		Block block = Block.blocksList[blockID];
		if (block == null) return false;
		if (block.getExplosionResistance(this) >= resistance) return false;
		Material material = block.blockMaterial;
		return !material.isReplaceable() && !(material == Material.water || material == Material.lava);
	}

	private static final int waterID = Block.waterStill.blockID;
	private static final int waterID2 = Block.waterMoving.blockID;
	private static boolean checkWater(World world, int x, int y, int z) {
		return world.getBlockId(x, y+1, z) == waterID
				|| world.getBlockId(x-1, y, z) == waterID
				|| world.getBlockId(x, y, z-1) == waterID
				|| world.getBlockId(x+1, y, z) == waterID
				|| world.getBlockId(x, y, z+1) == waterID;
	}

	private static Stream<ChunkCoordIntPair> rangeClosed(final ChunkCoordIntPair start, final ChunkCoordIntPair end) {
		int i = Math.abs(start.chunkXPos - end.chunkXPos);
		int j = Math.abs(start.chunkZPos - end.chunkZPos);
		final int k = start.chunkXPos < end.chunkXPos ? 1 : -1;
		final int l = start.chunkZPos < end.chunkZPos ? 1 : -1;
		return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkCoordIntPair>((long) i * j, Spliterator.SIZED) {
			private ChunkCoordIntPair cp;

			@Override
			public boolean tryAdvance(Consumer<? super ChunkCoordIntPair> action) {
				if (this.cp == null) this.cp = start;
				else {
					int i1 = this.cp.chunkXPos;
					int j1 = this.cp.chunkZPos;
					if (i1 == end.chunkXPos) {
						if (j1 == end.chunkZPos) {
							return false;
						}

						this.cp = new ChunkCoordIntPair(start.chunkXPos, j1 + l);
					} else {
						this.cp = new ChunkCoordIntPair(i1 + k, j1);
					}
				}

				action.accept(this.cp);
				return true;
			}
		}, false);
	}
}
