package com.devotedmc.testbukkit;

import static org.mockito.Mockito.*;

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

public abstract class TestInventory implements Inventory {
	
	private InventoryHolder holder;
	private InventoryType type;
	private int size;
	private LinkedHashMap<Integer, ItemStack> items;
	
	public List<HumanEntity> viewers = new LinkedList<HumanEntity>();
	
	public static TestInventory create(final InventoryHolder holder, final InventoryType type) {
		
		Class<? extends Inventory> invClass = Inventory.class;
		
		switch(type) {
		case ANVIL:
			invClass = AnvilInventory.class;
			break;
		case BREWING:
			invClass = BrewerInventory.class;
			break;
		case CRAFTING:
			invClass = CraftingInventory.class;
			break;
		case ENCHANTING:
			invClass = EnchantingInventory.class;
			break;
		case FURNACE:
			invClass = FurnaceInventory.class;
			break;
		case MERCHANT:
			invClass = MerchantInventory.class;
			break;
		case PLAYER:
			invClass = PlayerInventory.class;
			break;
		default:
			break;
		}
		
		TestInventory inv = mock(TestInventory.class, withSettings().extraInterfaces(invClass));
		
		inv.holder = holder;
		inv.type = type;
		inv.size = type.getDefaultSize();
		inv.items = new LinkedHashMap<Integer, ItemStack>();
		
		when(inv.getSize()).thenCallRealMethod();
		when(inv.getMaxStackSize()).thenCallRealMethod();
		doNothing().when(inv).setMaxStackSize(anyInt());
		when(inv.getItem(anyInt())).thenCallRealMethod();
		doCallRealMethod().when(inv).setItem(anyInt(), any());
		when(inv.addItem()).thenCallRealMethod();
		when(inv.removeItem()).thenCallRealMethod();
		when(inv.getContents()).thenCallRealMethod();
		doCallRealMethod().when(inv).setContents(any());
		when(inv.getStorageContents()).thenCallRealMethod();
		doCallRealMethod().when(inv).setStorageContents(any());
		when(inv.contains(any(Material.class))).thenCallRealMethod();
		when(inv.contains(any(ItemStack.class))).thenCallRealMethod();
		when(inv.first(any(Material.class))).thenCallRealMethod();
		when(inv.first(any(ItemStack.class))).thenCallRealMethod();
		when(inv.first(anyInt())).thenCallRealMethod();
		when(inv.firstEmpty()).thenCallRealMethod();
		doCallRealMethod().when(inv).remove(anyInt());
		doCallRealMethod().when(inv).remove(any(ItemStack.class));
		doCallRealMethod().when(inv).remove(any(Material.class));
		doCallRealMethod().when(inv).remove(anyInt());
		doCallRealMethod().when(inv).clear(anyInt());
		doCallRealMethod().when(inv).clear();
		when(inv.getViewers()).thenCallRealMethod();
		when(inv.getTitle()).thenCallRealMethod();
		when(inv.getType()).thenCallRealMethod();
		when(inv.getHolder()).thenCallRealMethod();
		when(inv.getLocation()).thenCallRealMethod();
		
		return inv;
	}
		
		
	
	public TestInventory(final InventoryHolder holder, InventoryType type) {
	}

	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public void setMaxStackSize(int size) {
	}

	@Override
	public String getName() {
		return "TestInventory";
	}

	@Override
	public ItemStack getItem(int index) {
		return items.get(index);
	}

	@Override
	public void setItem(int index, ItemStack item) {
		items.put(index, item);
	}

	@Override
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

	@Override
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

	@Override
	public ItemStack[] getContents() {
		return (ItemStack[])items.values().toArray();
	}

	@Override
	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		for(int i = 0; i < items.length; i++) {
			this.items.put(i, items[i]);
		}
	}

	@Override
	public ItemStack[] getStorageContents() {
		return getContents();
	}

	@Override
	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		setContents(items);
	}

	@Override
	public boolean contains(int materialId) {
		return first(materialId) != -1;
	}

	@Override
	public boolean contains(Material material) throws IllegalArgumentException {
		return first(material) != -1;
	}

	@Override
	public boolean contains(ItemStack item) {
		return first(item) != -1;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int first(int materialId) {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().getTypeId() == materialId) {
				return entry.getKey();
			}
		}
		return -1;
	}

	@Override
	public int first(Material material) throws IllegalArgumentException {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().getType().equals(material)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	@Override
	public int first(ItemStack item) {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(item)) {
				return entry.getKey();
			}
		}
		return -1;
	}

	@Override
	public int firstEmpty() {
		for (int i = 0; i < size; i++) {
			if (items.get(i) == null) {
				return i;
			}
		}
		return -1;
	}

	@SuppressWarnings("deprecation")
	@Override
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

	@Override
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

	@Override
	public void remove(ItemStack item) {
		for(Entry<Integer, ItemStack> entry: items.entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(item)) {
				items.remove(entry.getKey());
				return;
			}
		}
	}

	@Override
	public void clear(int index) {
		items.remove(index);
	}

	@Override
	public void clear() {
		items.clear();
	}

	@Override
	public List<HumanEntity> getViewers() {
		return viewers;
	}

	@Override
	public String getTitle() {
		return type.getDefaultTitle();
	}

	@Override
	public InventoryType getType() {
		return type;
	}

	@Override
	public InventoryHolder getHolder() {
		return holder;
	}

	@Override
	public Location getLocation() {
		return ((BlockState)holder).getLocation();
	}
}
