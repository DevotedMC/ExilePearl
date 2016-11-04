package com.devotedmc.testbukkit.core;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.WorldType;
import org.bukkit.block.Block;

import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestChunk;
import com.devotedmc.testbukkit.TestLocation;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@SuppressWarnings("deprecation")
@ProxyTarget(TestWorld.class)
public class CoreTestWorld extends ProxyMockBase<TestWorld> {

	public String name;
	public UUID uid;
	public Environment env;
	public WorldType worldType;
	public File worldFolder;
    public final Map<Long, TestChunk> chunks = new LinkedHashMap<Long, TestChunk>();
	
	public HashSet<TestChunk> loadedChunks;
	
	public CoreTestWorld(String name, Environment env, WorldType type) {
		super(TestWorld.class);
		this.name = name;
		this.uid = UUID.randomUUID();
		this.env = env;
		this.worldType = type;
		this.loadedChunks = new HashSet<TestChunk>();
	}
	
	@ProxyStub
	public String getName() {
		return name;
	}
	
	@ProxyStub
	public UUID getUID() {
		return uid;
	}
	
	@ProxyStub
	public Environment getEnvironment() {
		return env;
	}
	
	@ProxyStub
	public WorldType getWorldType() {
		return worldType;
	}
	
	@ProxyStub
	public File getWorldFolder() {
		return worldFolder;
	}
	
	@ProxyStub
	public TestBlock getBlockAt(Location l) {
        return getBlockAt(l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}
	
	@ProxyStub
	public TestBlock getBlockAt(int x, int y, int z) {
        return getChunkAt(x >> 4, z >> 4).getBlock(x & 0xF, y, z & 0xF);
	}
	
	@ProxyStub
	public TestChunk getChunkAt(Location l) {
        return getChunkAt(l.getBlockX() >> 4, l.getBlockZ() >> 4);
	}
	
	@ProxyStub
	public TestChunk getChunkAt(Block b) {
		return getChunkAt(b.getLocation());
	}
	
	@ProxyStub
	public TestChunk getChunkAt(int x, int z) {
		Long key = getLongChunkCoord(x, z);
		TestChunk c = chunks.get(key);
		if (c == null) {
			c = createInstance(TestChunk.class, getProxy(), x, z);
			chunks.put(key, c);
		}
		return c;
	}
	
	@ProxyStub
	public TestChunk[] getLoadedChunks() {
		TestChunk[] chunks = new TestChunk[this.loadedChunks.size()];
		
		int i = 0;
		for (TestChunk c : loadedChunks) {
			chunks[i++] = c;
		}
		
		return chunks;
	}

	@ProxyStub
	public int getHighestBlockYAt(int x, int z) {
		return 63;
	}
	
	@ProxyStub
	public int getMaxHeight() {
		if (env == Environment.NETHER) {
			return 125;
		}
		return 255;
	}

	@ProxyStub
	public int getBlockTypeIdAt(int x, int y, int z) {
		return getBlockAt(x, y, z).getTypeId();
	}
	
	@ProxyStub
	public TestLocation getSpawnLocation() {
		return new TestLocation(getProxy(), 0, 64, 0);
	}
	
    @Override
    public String toString() {
        return "TestWorld{name=" + getName() + '}';
    }
    
    private static Long getLongChunkCoord(int x, int z) {
    	return (long)x << 32 | z;
    }
}
