package com.devotedmc.testbukkit.core;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.BrewerInventory;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.FurnaceInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.MaterialData;

import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestBlockState;
import com.devotedmc.testbukkit.TestInventory;
import com.devotedmc.testbukkit.TestLocation;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@SuppressWarnings({ "deprecation" })
@ProxyTarget(TestBlockState.class)
class CoreTestBlockState extends ProxyMockBase<TestBlockState> {

	private TestBlock block;
	private MaterialData data;

	public CoreTestBlockState(TestBlock block, Class<?> interfaces) {
		super(TestBlockState.class, interfaces);

		this.block = block;
		createData(block.getData());
	}

	private void createData(final byte data) {
		Material mat = getType();
		if (mat == null || mat.getData() == null) {
			this.data = new MaterialData(block.getType(), data);
		} else {
			this.data = mat.getNewData(data);
		}
	}

	@ProxyStub
	public TestBlock getBlock() {
		return block;
	}

	@ProxyStub
	public Material getType() {
		return block.getType();
	}

	@ProxyStub
	public byte getLightLevel() {
		return getBlock().getLightLevel();
	}

	@ProxyStub
	public TestWorld getWorld() {
		return block.getWorld();
	}

	@ProxyStub
	public int getX() {
		return block.getX();
	}

	@ProxyStub
	public int getY() {
		return block.getY();
	}

	@ProxyStub
	public int getZ() {
		return block.getZ();
	}

	@ProxyStub
	public TestLocation getLocation() {
		return block.getLocation();
	}

	@ProxyStub
	public Location getLocation(Location loc) {
		if (loc != null) {
			loc.setWorld(block.getWorld());
			loc.setX(block.getX());
			loc.setY(block.getY());
			loc.setZ(block.getZ());
			loc.setYaw(0);
			loc.setPitch(0);
		}

		return loc;
	}

	@ProxyStub
	public Chunk getChunk() {
		return block.getLocation().getChunk();
	}

	@ProxyStub
	public void setData(MaterialData data) {
		this.data = data;
	}

	@ProxyStub
	public void setType(Material type) {
		block.setType(type);
	}

	@ProxyStub
	public boolean update() {
		return true;
	}

	@ProxyStub
	public boolean update(boolean force) {
		return true;
	}

	@ProxyStub
	public boolean update(boolean force, boolean applyPhysics) {
		return true;
	}

	@ProxyStub
	public int getTypeId() {
		return block.getTypeId();
	}

	@ProxyStub
	public boolean setTypeId(int type) {
		return block.setTypeId(type);
	}

	@ProxyStub
	public byte getRawData() {
		return block.getData();
	}

	@ProxyStub
	public void setRawData(byte data) {
		block.setData(data);
	}

	@ProxyStub
	public boolean isPlaced() {
		return true;
	}

	@ProxyStub
	public MaterialData getData() {
		return data;
	}


	public Inventory getInventory() {		
		InventoryType invType = null;
		Class<? extends Inventory> invClass = Inventory.class;

		switch(getType()) {
		case CHEST:
		case TRAPPED_CHEST:
			invType = InventoryType.CHEST;
			break;
		case DISPENSER:
			invType = InventoryType.DISPENSER;
			break;
		case DROPPER:
			invType = InventoryType.DROPPER;
			break;
		case FURNACE:
			invType = InventoryType.FURNACE;
			break;
		case WORKBENCH:
			invType = InventoryType.WORKBENCH;
			break;
		case ENCHANTMENT_TABLE:
			invType = InventoryType.ENCHANTING;
			break;
		case ENDER_CHEST:
			invType = InventoryType.ENDER_CHEST;
			break;
		case ANVIL:
			invType = InventoryType.ANVIL;
			break;
		case BEACON:
			invType = InventoryType.BEACON;
			break;
		case HOPPER:
			invType = InventoryType.HOPPER;
			break;
		case BREWING_STAND:
			invType = InventoryType.BREWING;
			break;
		default:
			return null;
		}

		switch(invType) {
		case ANVIL:
			invClass = AnvilInventory.class;
			break;
		case BEACON:
			invClass = BeaconInventory.class;
			break;
		case BREWING:
			invClass = BrewerInventory.class;
			break;
		case ENCHANTING:
			invClass = EnchantingInventory.class;
			break;
		case FURNACE:
			invClass = FurnaceInventory.class;
			break;
		case CHEST:
		default:
			break;
		}

		Inventory inventory = createInstance(TestInventory.class, getProxy(), invType, invClass);

		if (invType == InventoryType.CHEST) {
			int x = getX();
			int y = getY();
			int z = getZ();
			TestWorld world = getWorld();

			int id;
			if (world.getBlockTypeIdAt(x, y, z) == Material.CHEST.getId()) {
				id = Material.CHEST.getId();
			} else if (world.getBlockTypeIdAt(x, y, z) == Material.TRAPPED_CHEST.getId()) {
				id = Material.TRAPPED_CHEST.getId();
			} else {
				return inventory;
			}
			Inventory left = null;
			Inventory right = null;

			if (world.getBlockTypeIdAt(x - 1, y, z) == id) {
				left = createInstance(TestInventory.class, world.getBlockAt(x - 1, y, z).getState(), invType, invClass);
				right = inventory;
			}
			if (world.getBlockTypeIdAt(x + 1, y, z) == id) {
				left = inventory;
				right = createInstance(TestInventory.class, world.getBlockAt(x + 1, y, z).getState(), invType, invClass);
			}
			if (world.getBlockTypeIdAt(x, y, z - 1) == id) {
				left = createInstance(TestInventory.class, world.getBlockAt(x, y, z - 1).getState(), invType, invClass);
				right = inventory;
			}
			if (world.getBlockTypeIdAt(x, y, z + 1) == id) {
				left = inventory;
				right = createInstance(TestInventory.class, world.getBlockAt(x, y, z + 1).getState(), invType, invClass);
			}
			if (left != null && right != null) {
				return createInstance(DoubleChestInventory.class, left, right);
			}
		}

		return inventory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		o = getEqualsProxy(o);

		if (getClass() != o.getClass()) {
			return false;
		}

		CoreTestBlockState other = (CoreTestBlockState) o;

		return new EqualsBuilder()
				.append(block, other.block)
				.append(data, other.data)
				.isEquals();
	}
}
