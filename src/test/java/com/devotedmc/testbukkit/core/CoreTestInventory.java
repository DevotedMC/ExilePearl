package com.devotedmc.testbukkit.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import com.devotedmc.testbukkit.TestInventory;
import com.devotedmc.testbukkit.annotation.ProxyStub;
import com.devotedmc.testbukkit.annotation.ProxyTarget;

@ProxyTarget(TestInventory.class)
public class CoreTestInventory extends ProxyMockBase<TestInventory> {
	
	private InventoryHolder holder;
	private InventoryType type;
	private int size;
	private LinkedHashMap<Integer, ItemStack> items;
	private List<HumanEntity> viewers = new LinkedList<HumanEntity>();
	
	public CoreTestInventory(final InventoryHolder holder, final InventoryType type, Class<? extends Inventory> holderType) {
		super(TestInventory.class, holderType);
		
		this.holder = holder;
		this.type = type;
		this.size = type.getDefaultSize();
		this.items = new LinkedHashMap<Integer, ItemStack>();
	}
	
	public CoreTestInventory(final InventoryHolder holder, final InventoryType type) {
		this(holder, type, null);
	}

	@ProxyStub
	public int getSize() {
		return size;
	}

	@ProxyStub
	public int getMaxStackSize() {
		return 64;
	}

	@ProxyStub
	public String getName() {
		return "TestInventory";
	}

	@ProxyStub
	public ItemStack getItem(int index) {
		return items.get(index);
	}

	@ProxyStub
	public void setItem(int index, ItemStack item) {
		items.put(index, item);
	}

	@ProxyStub
	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> notAdded = new HashMap<Integer, ItemStack>();
		
		for(int i = 0; i < items.length; i++) {
			boolean added = false;
			for(int j = 0; j < size; j++) {
				if (this.items.get(j) == null) {
					this.items.put(j, items[i]);
					added = true;
					continue;
				}
			}
			
			if (!added) {
				notAdded.put(i, items[i]);
			}
		}
		return notAdded;
	}

	@ProxyStub
	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		HashMap<Integer, ItemStack> notRemoved = new HashMap<Integer, ItemStack>();
		
		for(int i = 0; i < items.length; i++) {
			boolean removed = false;
			for(int j = 0; j < size; j++) {
				if (this.items.get(j) != null) {
					this.items.remove(j);
					removed = true;
					continue;
				}
			}
			
			if (!removed) {
				notRemoved.put(i, items[i]);
			}
		}
		return notRemoved;
	}

	@ProxyStub
	public ItemStack[] getContents() {
		return (ItemStack[])items.values().toArray();
	}

	@ProxyStub
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		for(int i = 0; i < items.length; i++) {
			this.items.put(i, items[i]);
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
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().getTypeId() == materialId) {
				return entry.getKey();
			}
		}
		return -1;
	}

	@ProxyStub
	public int first(Material material) throws IllegalArgumentException {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().getType().equals(material)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	@ProxyStub
	public int first(ItemStack item) {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(item)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	@ProxyStub
	public int firstEmpty() {
		for (int i = 0; i < size; i++) {
			if (items.get(i) == null) {
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("deprecation")
	@ProxyStub
	public void remove(int materialId) {
		Iterator<ItemStack> iter = items.values().iterator();
		while(iter.hasNext()) {
			ItemStack is = iter.next();
			if (is == null) {
				break;
			}
			if (is.getTypeId() == materialId) {
				iter.remove();
			}
		}
	}

	@ProxyStub
	public void remove(Material material) throws IllegalArgumentException {
		Iterator<ItemStack> iter = items.values().iterator();
		while(iter.hasNext()) {
			ItemStack is = iter.next();
			if (is == null) {
				break;
			}
			if (is.getType() == material) {
				iter.remove();
			}
		}
	}

	@ProxyStub
	public void remove(ItemStack item) {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(item)) {
				items.remove(entry.getKey());
				return;
			}
		}
	}

	@ProxyStub
	public void clear(int index) {
		items.remove(index);
	}

	@ProxyStub
	public void clear() {
		items.clear();
	}

	@ProxyStub
	public List<HumanEntity> getViewers() {
		return viewers;
	}

	@ProxyStub
	public String getTitle() {
		return type.getDefaultTitle();
	}

	@ProxyStub
	public InventoryType getType() {
		return type;
	}

	@ProxyStub
	public InventoryHolder getHolder() {
		return holder;
	}

	@ProxyStub
	public Location getLocation() {
		return ((BlockState)holder).getLocation();
	}
}
