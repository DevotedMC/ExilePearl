package com.devotedmc.testbukkit;

import static org.mockito.Mockito.*;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.*;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.MaterialData;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

@SuppressWarnings("deprecation")
public abstract class TestBlockState implements BlockState {
	
	public TestBlock block;
	public MaterialData data;
	public byte lightLevel;

	public static TestBlockState create(TestBlock block) {
		
		Class<? extends BlockState> stateClass = BlockState.class;
		InventoryType invType = null;
		
		switch(block.getType()) {
		case CHEST:
		case TRAPPED_CHEST:
			stateClass = Chest.class;
			invType = InventoryType.CHEST;
			break;
		case BEACON:
			stateClass = Beacon.class;
			invType = InventoryType.BEACON;
			break;
		case BREWING_STAND:
			stateClass = BrewingStand.class;
			invType = InventoryType.BREWING;
			break;
		case DISPENSER:
			stateClass = Dispenser.class;
			invType = InventoryType.DISPENSER;
			break;
		case DROPPER:
			stateClass = Dropper.class;
			invType = InventoryType.DROPPER;
			break;
		case FURNACE:
			stateClass = Furnace.class;
			invType = InventoryType.FURNACE;
			break;
		case HOPPER:
			stateClass = Hopper.class;
			invType = InventoryType.HOPPER;
			break;
		default:
			break;
		}
		
		TestBlockState bs = mock(TestBlockState.class, withSettings().extraInterfaces(stateClass));
		if (stateClass.asSubclass(InventoryHolder.class) != null && invType != null) {
			InventoryHolder holder = (InventoryHolder)bs;
			final Inventory inv = TestInventory.create(holder, invType);
			when(holder.getInventory()).then(new Answer<Inventory>() {

				@Override
				public Inventory answer(InvocationOnMock invocation) throws Throwable {
					TestBlockState state = (TestBlockState)invocation.getMock();
					Inventory inventory = ((InventoryHolder)state).getInventory();
			        int x = state.getX();
			        int y = state.getY();
			        int z = state.getZ();
			        TestWorld world = state.getWorld();
			        
			        int id;
			        if (world.getBlockTypeIdAt(x, y, z) == Material.CHEST.getId()) {
			            id = Material.CHEST.getId();
			        } else if (world.getBlockTypeIdAt(x, y, z) == Material.TRAPPED_CHEST.getId()) {
			            id = Material.TRAPPED_CHEST.getId();
			        } else {
			            throw new IllegalStateException("CraftChest is not a chest but is instead " + world.getBlockAt(x, y, z));
			        }
			        /*
			        if (world.getBlockTypeIdAt(x - 1, y, z) == id) {
			        	Inventory left = TestInventory.create((InventoryHolder)TestBlockState.create(world.getBlockAt(x -1, y, z)), InventoryType.CHEST);
			        	Inventory right = inventory;
			        	
			        	Inventory left = new CraftInventory((TileEntityChest)world.getHandle().getTileEntity(new BlockPosition(x - 1, y, z)));
			            inventory = new CraftInventoryDoubleChest(left, inventory);
			        }
			        if (world.getBlockTypeIdAt(x + 1, y, z) == id) {
			        	Inventory right = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x + 1, y, z)));
			            inventory = new CraftInventoryDoubleChest(inventory, right);
			        }
			        if (world.getBlockTypeIdAt(x, y, z - 1) == id) {
			        	Inventory left = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x, y, z - 1)));
			            inventory = new CraftInventoryDoubleChest(left, inventory);
			        }
			        if (world.getBlockTypeIdAt(x, y, z + 1) == id) {
			        	Inventory right = new CraftInventory((TileEntityChest) world.getHandle().getTileEntity(new BlockPosition(x, y, z + 1)));
			            inventory = new CraftInventoryDoubleChest(inventory, right);
			        } */ // TODO
					
					
					
					return inventory;
				}
			});
		}
		
		when(bs.getBlock()).thenCallRealMethod();
		when(bs.getData()).thenCallRealMethod();
		when(bs.getType()).thenCallRealMethod();
		when(bs.getLightLevel()).thenCallRealMethod();
		when(bs.getBlock()).thenCallRealMethod();
		when(bs.getBlock()).thenCallRealMethod();
		
		
		bs.block = block;
		bs.data = new MaterialData(bs.getType());
		bs.lightLevel = 0;
		return bs;
	}
	
	private TestBlockState() { }
	
	@Override
    public Block getBlock() {
    	return block;
    }

	@Override
    public MaterialData getData() {
    	return data;
    }

	@Override
    public Material getType() {
    	return block.type;
    }
	
	@Override
    public byte getLightLevel() {
		return lightLevel;
	}
	
	@Override
    public TestWorld getWorld() {
		return block.getWorld();
	}
	
	@Override
    public int getX() {
		return block.getX();
	}
	
	@Override
    public int getY() {
		return block.getY();
	}
	
	@Override
    public int getZ() {
		return block.getZ();
	}
	
	@Override
    public Location getLocation() {
		return block.getLocation();
	}
	
	@Override
    public Location getLocation(Location loc) {
		if (loc == null) {
			return null;
		}
		
		Location l = block.getLocation();
		loc.setWorld(l.getWorld());
		loc.setX(l.getX());
		loc.setY(l.getY());
		loc.setZ(l.getZ());
		return loc;
	}
	
	@Override
    public Chunk getChunk() {
		return block.getLocation().getChunk();
	}
	
	@Override
    public void setData(MaterialData data) {
		this.data = data;
	}
	
	@Override
    public void setType(Material type) {
		block.setType(type);
	}
	
	@Override
    public boolean update() {
		return true;
	}
	
	@Override
    public boolean update(boolean force) {
		return true;
	}
	
	@Override
    public boolean update(boolean force, boolean applyPhysics) {
		return true;
	}

	@Override
	public int getTypeId() {
		return block.getTypeId();
	}

	@Override
	public boolean setTypeId(int type) {
		return block.setTypeId(type);
	}

	@Override
	public byte getRawData() {
		return this.data.getData();
	}

	@Override
	public void setRawData(byte data) {
		this.data.setData(data);
	}

	@Override
	public boolean isPlaced() {
		return true;
	}
}
