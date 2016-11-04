package com.devotedmc.testbukkit.core;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;

import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestChunk;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@SuppressWarnings("deprecation")
@ProxyTarget(TestChunk.class)
public class CoreTestChunk extends ProxyMockBase<TestChunk> {
	
	private final TestWorld world;
	private final int x;
	private final int z;
	private boolean isLoaded;

    public final Map<BigInteger, TestBlock> blocks = new LinkedHashMap<BigInteger, TestBlock>();
	
	public CoreTestChunk(TestWorld world, int x, int z) {
		super(TestChunk.class);
		this.world = world;
		this.x = x;
		this.z = z;
	}
	
	@ProxyStub
	public int getX() {
    	return x;
    }

	@ProxyStub
    public int getZ() {
		return z;
	}

	@ProxyStub
	public TestWorld getWorld() {
		return world;
	}

	@ProxyStub
	public TestBlock getBlock(int x, int y, int z) {
		TestBlock b = blocks.get(getBlockKeyCoord(x, y, z));
		if (b == null) {
			b = createInstance(TestBlock.class, getProxy(), (getX() << 4) | (x & 0xF), y, (getZ() << 4) | (z & 0xF));
			
			if(y < 64) {
				b.setType(Material.DIRT);
			}
		}
		return b;
	}

	@ProxyStub
	public Entity[] getEntities() {
		return new Entity[0];
	}

	@ProxyStub
	public BlockState[] getTileEntities() {
		return new BlockState[0];
	}

	@ProxyStub
	public boolean isLoaded() {
		return isLoaded;
	}

	@ProxyStub
	public boolean load() {
        return getWorld().loadChunk(getX(), getZ(), true);
	}

	@ProxyStub
	public boolean load(boolean generate) {
        return getWorld().loadChunk(getX(), getZ(), generate);
	}

	@ProxyStub
	public boolean unload() {
        return getWorld().unloadChunk(getX(), getZ());
	}

	@ProxyStub
	public boolean unload(boolean save) {
        return getWorld().unloadChunk(getX(), getZ(), save);
	}

	@ProxyStub
	public boolean unload(boolean save, boolean safe) {
        return getWorld().unloadChunk(getX(), getZ(), save, safe);
	}
	
    @Override
    public String toString() {
        return "TestChunk{" + "x=" + getX() + "z=" + getZ() + '}';
    }
    
    private static BigInteger getBlockKeyCoord(int x, int y, int z) {
    	
    	BigInteger bigX = BigInteger.valueOf((long)x);
    	BigInteger bigY = BigInteger.valueOf((long)y);
    	BigInteger bigZ = BigInteger.valueOf((long)z);
    	return bigX.add(bigY).add(bigZ);
    }
}
