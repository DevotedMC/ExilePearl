package com.devotedmc.ExilePearl.broadcast;

import java.util.UUID;

import com.devotedmc.ExilePearl.util.NameLayerPermissions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.devotedmc.ExilePearl.ExilePearl;
import com.devotedmc.ExilePearl.Lang;

import vg.civcraft.mc.civmodcore.util.Guard;
import vg.civcraft.mc.civmodcore.util.TextUtil;
import vg.civcraft.mc.namelayer.core.Group;
import vg.civcraft.mc.namelayer.mc.GroupAPI;
import vg.civcraft.mc.namelayer.mc.NameLayerPlugin;

public class NLGroupBroadcastListener implements BroadcastListener {

	private final Group group;

	public NLGroupBroadcastListener(final Group group) {
		Guard.ArgumentNotNull(group, "group");

		this.group = group;
	}

	@Override
	public void broadcast(ExilePearl pearl) {
		Location l = pearl.getHolder().getLocation();
		String name = pearl.getHolder().getName();

		String msg = TextUtil.parse(Lang.groupPearlBroadcast, group.getName(), pearl.getPlayerName(), name, l.getBlockX(), l.getBlockY(), l.getBlockZ(), l.getWorld().getName());

		if (!GroupAPI.hasPermission(pearl.getPlayerId(), group, NameLayerPlugin.getInstance().getGroupTracker().getPermissionTracker().getPermission(NameLayerPermissions.ALLOW_EXILE_BROADCAST))) return;

		for (UUID uid : group.getAllMembers()) {
			Player p = Bukkit.getPlayer(uid);
			if (p != null && p.isOnline()) {
				p.sendMessage(msg);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        NLGroupBroadcastListener other = (NLGroupBroadcastListener) o;

		return group.equals(other.group);
	}

	@Override
	public int hashCode() {
		return group.hashCode();
	}

	@Override
	public boolean contains(Object o) {
		return group.equals(o);
	}
}
