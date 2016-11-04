package com.devotedmc.testbukkit.core;

import java.util.Arrays;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.material.MaterialData;

import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestBlockState;
import com.devotedmc.testbukkit.TestLocation;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@SuppressWarnings({ "deprecation" })
@ProxyTarget(TestBlockState.class)
 class CoreTestBlockState extends ProxyMockBase<TestBlockState> {
	
	private final Class<? extends BlockState> stateType;
	private TestBlock block;
	private MaterialData data;

	public CoreTestBlockState(TestBlock block, Class<? extends BlockState> stateType) {
		super(TestBlockState.class, stateType);
		
		this.block = block;
		this.stateType = stateType;
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
		if (!Arrays.asList(stateType.getInterfaces()).contains(InventoryHolder.class)) {
			return null;
		}
		
    	return null; // TODO
    }
}
