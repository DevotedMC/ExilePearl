package com.devotedmc.testbukkit.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@ProxyTarget(DoubleChestInventory.class)
public class CoreTestInventoryDoubleChest extends ProxyMockBase<DoubleChestInventory> {
	
	private Inventory left;
	private Inventory right;
	
	public CoreTestInventoryDoubleChest(final Inventory left, final Inventory right) {
		super(DoubleChestInventory.class);
		
		this.left = left;
		this.right = right;
	}

	@ProxyStub
	public int getSize() {
		return left.getSize() + right.getSize();
	}

	@ProxyStub
	public int getMaxStackSize() {
		return left.getMaxStackSize() + right.getMaxStackSize();
	}

	@ProxyStub
	public String getName() {
		return "DoubleChestInventory";
	}

	@ProxyStub
	public ItemStack getItem(int index) {
		return getRealInventory(index).getItem(getRealIndex(index));
	}

	@ProxyStub
	public void setItem(int index, ItemStack item) {
		getRealInventory(index).setItem(getRealIndex(index), item);
	}

	@ProxyStub
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> notAdded = new HashMap<Integer, ItemStack>();
		Map<Integer, ItemStack> notAddedLeft = left.addItem(items);
		Map<Integer, ItemStack> notAddedRight = right.addItem(items);
		
		for(int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (notAddedLeft.containsValue(item) && notAddedRight.containsValue(item)) {
				notAdded.put(i, item);
			}
		}
		
		return notAdded;
	}

	@ProxyStub
	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> notRemoved = new HashMap<Integer, ItemStack>();
		Map<Integer, ItemStack> notRemovedLeft = left.removeItem(items);
		Map<Integer, ItemStack> notRemovedRight = left.removeItem(items);
		
		for(int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if (notRemovedLeft.containsValue(item) && notRemovedRight.containsValue(item)) {
				notRemoved.put(i, item);
			}
		}
		
		return notRemoved;
	}

	@ProxyStub
	public ItemStack[] getContents() {
		ItemStack[] contents = new ItemStack[getSize()];
		System.arraycopy(left.getContents(), 0, contents, 0, left.getSize());
		System.arraycopy(right.getContents(), 0, contents, left.getSize(), right.getSize());
		return contents;
	}

	@ProxyStub
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
        if (getSize() < items.length) {
            throw new IllegalArgumentException("Invalid inventory size; expected " + getSize() + " or less");
        }
        ItemStack[] leftItems = new ItemStack[left.getSize()], rightItems = new ItemStack[right.getSize()];
        System.arraycopy(items, 0, leftItems, 0, Math.min(left.getSize(),items.length));
        left.setContents(leftItems);
        if (items.length >= left.getSize()) {
            System.arraycopy(items, left.getSize(), rightItems, 0, Math.min(right.getSize(), items.length - left.getSize()));
            right.setContents(rightItems);
        }
	}

	@ProxyStub
	public ItemStack[] getStorageContents() {
		return getContents();
	}

	@ProxyStub
	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		setContents(items);
	}

	@ProxyStub
	public boolean contains(int materialId) {
		return first(materialId) != -1;
	}

	@ProxyStub
	public boolean contains(Material material) throws IllegalArgumentException {
		return first(material) != -1;
	}

	@ProxyStub
	public boolean contains(ItemStack item) {
		return first(item) != -1;
	}

	@SuppressWarnings("deprecation")
	@ProxyStub
	public int first(int materialId) {
		int result = left.first(materialId);
		if (result == -1) {
			result = right.first(materialId);
			if (result != -1) {
				result += left.getSize();
			}
		}
		return result;
	}

	@ProxyStub
	public int first(Material material) throws IllegalArgumentException {
		int result = left.first(material);
		if (result == -1) {
			result = right.first(material);
			if (result != -1) {
				result += left.getSize();
			}
		}
		return result;
	}

	@ProxyStub
	public int first(ItemStack item) {
		int result = left.first(item);
		if (result == -1) {
			result = right.first(item);
			if (result != -1) {
				result += left.getSize();
			}
		}
		return result;
	}

	@ProxyStub
	public int firstEmpty() {
		int result = left.firstEmpty();
		if (result == -1) {
			result = right.firstEmpty();
			if (result != -1) {
				result += left.getSize();
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@ProxyStub
	public void remove(int materialId) {
		left.remove(materialId);
		right.remove(materialId);
	}

	@ProxyStub
	public void remove(Material material) throws IllegalArgumentException {
		left.remove(material);
		right.remove(material);
	}

	@ProxyStub
	public void remove(ItemStack item) {
		left.remove(item);
		right.remove(item);
	}

	@ProxyStub
	public void clear(int index) {
		getRealInventory(index).clear(getRealIndex(index));
	}

	@ProxyStub
	public void clear() {
		left.clear();
		right.clear();
	}

	@ProxyStub
	public List<HumanEntity> getViewers() {
		List<HumanEntity> viewers = new ArrayList<HumanEntity>();
		viewers.addAll(left.getViewers());
		viewers.addAll(right.getViewers());
		return viewers;
	}

	@ProxyStub
	public String getTitle() {
		return left.getTitle();
	}

	@ProxyStub
	public InventoryType getType() {
		return left.getType();
	}

	@ProxyStub
	public DoubleChest getHolder() {
        return new DoubleChest(getProxy());
	}

	@ProxyStub
	public Location getLocation() {
        return getLeftSide().getLocation().add(getRightSide().getLocation()).multiply(0.5);
	}

	@ProxyStub
	public Inventory getLeftSide() {
		return left;
	}

	@ProxyStub
	public Inventory getRightSide() {
		return right;
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

        CoreTestInventoryDoubleChest other = (CoreTestInventoryDoubleChest) o;

		return new EqualsBuilder()
				.append(left, other.left)
				.append(right, other.right)
				.isEquals();
    }
    
    private Inventory getRealInventory(int index) {
    	if (index < left.getSize()) {
    		return left;
    	}
    	return right;
    }
    
    private int getRealIndex(int index) {
    	if (index < left.getSize()) {
    		return index;
    	}
    	return index - left.getSize();
    }
}
