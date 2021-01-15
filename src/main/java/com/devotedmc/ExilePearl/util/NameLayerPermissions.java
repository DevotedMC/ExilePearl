/**
 * Created by Aleksey on 24.07.2017.
 */

package com.devotedmc.ExilePearl.util;

import vg.civcraft.mc.namelayer.core.DefaultPermissionLevel;
import vg.civcraft.mc.namelayer.core.PermissionTracker;
import vg.civcraft.mc.namelayer.core.PermissionType;
import vg.civcraft.mc.namelayer.mc.GroupAPI;

public class NameLayerPermissions {
	public final static String BASTION_ALLOW_EXILED = "BASTION_ALLOW_EXILED";
	public final static String ALLOW_EXILE_BROADCAST = "ALLOW_EXILE_BROADCAST";

	private PermissionTracker permTracker;

	public NameLayerPermissions(PermissionTracker permTracker) {
		this.permTracker = permTracker;
		setup();
	}

	private void setup() {
		GroupAPI.registerPermission(BASTION_ALLOW_EXILED, DefaultPermissionLevel.MEMBER, "Allows exiled players to enter bastions on this group.");
		GroupAPI.registerPermission(ALLOW_EXILE_BROADCAST, DefaultPermissionLevel.MEMBER, "Allows exiled players to broadcast their pearl location on this group.");
	}


	public PermissionType getBastionsAllowExiles() {
		return permTracker.getPermission(BASTION_ALLOW_EXILED);
	}

	public PermissionType getExilesCanBroadcast() {
		return permTracker.getPermission(ALLOW_EXILE_BROADCAST);
	}
}
