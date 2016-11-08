package com.devotedmc.testbukkit.core;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.BrewingStand;
import org.bukkit.block.Chest;
import org.bukkit.block.CommandBlock;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.Dispenser;
import org.bukkit.block.Dropper;
import org.bukkit.block.EndGateway;
import org.bukkit.block.FlowerPot;
import org.bukkit.block.Furnace;
import org.bukkit.block.Hopper;
import org.bukkit.block.Jukebox;
import org.bukkit.block.NoteBlock;
import org.bukkit.block.Sign;
import org.bukkit.block.Skull;

import com.devotedmc.testbukkit.TestBlock;
import com.devotedmc.testbukkit.TestBlockState;
import com.devotedmc.testbukkit.TestChunk;
import com.devotedmc.testbukkit.TestLocation;
import com.devotedmc.testbukkit.TestWorld;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@SuppressWarnings("deprecation")
@ProxyTarget(TestBlock.class)
public class CoreTestBlock extends ProxyMockBase<TestBlock> {

	private final TestChunk chunk;
	private final int x;
	private final int y;
	private final int z;
	private Material type;
	private byte rawData;
	
	public CoreTestBlock(TestChunk chunk, int x, int y, int z) {
		super(TestBlock.class);
		this.chunk = chunk;
		this.x = x;
		this.y = y;
		this.z = z;
		this.type = Material.AIR;
		this.rawData = 0;
	}
	
	public CoreTestBlock(Location l) {
		this((TestChunk)l.getChunk(), l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}
	
	@ProxyStub
	public Material getType() {
		return type;
	}
	
	@ProxyStub
	public void setType(Material type) {
		this.type = type;
		this.rawData = 0;
	}
	
	@ProxyStub
	public int getTypeId() {
		return type.getId();
	}
	
	@ProxyStub
	public TestWorld getWorld() {
		return chunk.getWorld();
	}
	
	@ProxyStub
	public int getX() {
		return x;
	}
	
	@ProxyStub
	public int getY() {
		return y;
	}
	
	@ProxyStub
	public int getZ() {
		return z;
	}
	
	@ProxyStub
	public TestLocation getLocation() {
		return new TestLocation(getWorld(), x, y, z);
	}
	
	@ProxyStub
	public boolean isEmpty() {
		return type == Material.AIR;
	}

	@ProxyStub
	public byte getData() {
		return rawData;
	}

	@ProxyStub
	public void setData(byte rawData) {
		this.rawData = rawData;
	}
	
    @Override
    public String toString() {
        return "TestBlock{" + "chunk=" + chunk + ",x=" + x + ",y=" + y + ",z=" + z + ",type=" + getType() + ",data=" + getData() + '}';
    }

	@ProxyStub
    public TestBlockState getState() {
		Class<? extends BlockState> stateClass = BlockState.class;
		
        switch (getType()) {
        case SIGN:
        case SIGN_POST:
        case WALL_SIGN:
        	stateClass = Sign.class;
		case CHEST:
		case TRAPPED_CHEST:
			stateClass = Chest.class;
			break;
		case BEACON:
			stateClass = Beacon.class;
			break;
		case BREWING_STAND:
			stateClass = BrewingStand.class;
			break;
		case DISPENSER:
			stateClass = Dispenser.class;
			break;
		case DROPPER:
			stateClass = Dropper.class;
			break;
		case FURNACE:
			stateClass = Furnace.class;
			break;
		case HOPPER:
			stateClass = Hopper.class;
			break;
        case END_GATEWAY:
			stateClass = EndGateway.class;
        	break;
        case MOB_SPAWNER:
			stateClass = CreatureSpawner.class;
        	break;
        case NOTE_BLOCK:
			stateClass = NoteBlock.class;
        	break;
        case JUKEBOX:
			stateClass = Jukebox.class;
        	break;
        case SKULL:
			stateClass = Skull.class;
        	break;
        case COMMAND:
        case COMMAND_CHAIN:
        case COMMAND_REPEATING:
			stateClass = CommandBlock.class;
        	break;
        case BANNER:
        case WALL_BANNER:
        case STANDING_BANNER:
			stateClass = Banner.class;
        	break;
        case FLOWER_POT:
			stateClass = FlowerPot.class;
        	break;
        default:
        	break;
        }
    	return createInstance(TestBlockState.class, getProxy(), stateClass);
    }
	
	@ProxyStub
    public byte getLightLevel() {
		return 0;
    }

	@ProxyStub
    public byte getLightFromSky() {
		return 0;
    }

	@ProxyStub
    public byte getLightFromBlocks() {
		return 0;
    }

    public TestBlock getFace(final BlockFace face) {
        return getRelative(face, 1);
    }

    public TestBlock getFace(final BlockFace face, final int distance) {
        return getRelative(face, distance);
    }

	@ProxyStub
    public TestBlock getRelative(final int modX, final int modY, final int modZ) {
        return getWorld().getBlockAt(getX() + modX, getY() + modY, getZ() + modZ);
    }

	@ProxyStub
    public TestBlock getRelative(BlockFace face) {
        return getRelative(face, 1);
    }

	@ProxyStub
    public TestBlock getRelative(BlockFace face, int distance) {
        return getRelative(face.getModX() * distance, face.getModY() * distance, face.getModZ() * distance);
    }

	@ProxyStub
    public BlockFace getFace(final Block block) {
        BlockFace[] values = BlockFace.values();

        for (BlockFace face : values) {
            if ((this.getX() + face.getModX() == block.getX()) &&
                (this.getY() + face.getModY() == block.getY()) &&
                (this.getZ() + face.getModZ() == block.getZ())
            ) {
                return face;
            }
        }

        return null;
    }
}
