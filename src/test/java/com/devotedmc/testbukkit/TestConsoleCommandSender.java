package com.devotedmc.testbukkit;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ManuallyAbandonedConversationCanceller;
import org.bukkit.permissions.PermissibleBase;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

public class TestConsoleCommandSender implements ConsoleCommandSender {
	
    protected final TestConversationTracker conversationTracker = new TestConversationTracker();
    private PermissibleBase perm;
    
    public TestConsoleCommandSender() {
    }

	@Override
	public void sendMessage(String message) {
        sendRawMessage(message);
	}

	@Override
	public void sendRawMessage(String message) {
        System.out.println(ChatColor.stripColor(message));
	}

	@Override
	public void sendMessage(String[] messages) {
        for (String message : messages) {
            sendMessage(message);
        }
	}

	@Override
	public Server getServer() {
        return Bukkit.getServer();
	}

	@Override
	public String getName() {
        return "CONSOLE";
	}

	@Override
	public boolean isPermissionSet(String name) {
        return getPerm().isPermissionSet(name);
	}

	@Override
	public boolean isPermissionSet(Permission perm) {
        return getPerm().isPermissionSet(perm);
	}

	@Override
	public boolean hasPermission(String name) {
        return getPerm().hasPermission(name);
	}

	@Override
	public boolean hasPermission(Permission perm) {
        return getPerm().hasPermission(perm);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        return getPerm().addAttachment(plugin, name, value);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin) {
        return getPerm().addAttachment(plugin);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        return getPerm().addAttachment(plugin, name, value, ticks);
	}

	@Override
	public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        return getPerm().addAttachment(plugin, ticks);
	}

	@Override
	public void removeAttachment(PermissionAttachment attachment) {
		getPerm().removeAttachment(attachment);
	}

	@Override
	public void recalculatePermissions() {
		getPerm().recalculatePermissions();
	}

	@Override
	public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return getPerm().getEffectivePermissions();
	}

	@Override
	public boolean isOp() {
        return true;
	}

	@Override
	public void setOp(boolean value) {
        throw new UnsupportedOperationException("Cannot change operator status of server console");
	}

	@Override
	public boolean isConversing() {
        return conversationTracker.isConversing();
	}

	@Override
	public void acceptConversationInput(String input) {
        conversationTracker.acceptConversationInput(input);
	}

	@Override
	public boolean beginConversation(Conversation conversation) {
        return conversationTracker.beginConversation(conversation);
	}

	@Override
	public void abandonConversation(Conversation conversation) {
        conversationTracker.abandonConversation(conversation, new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
	}

	@Override
	public void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        conversationTracker.abandonConversation(conversation, details);
	}
	
	private PermissibleBase getPerm() {
		if (perm == null) {
			perm = new PermissibleBase(this);
		}
		return perm;
	}
}
