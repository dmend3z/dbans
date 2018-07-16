package com.dnx.bans.comandos;

import com.dnx.bans.Main;
import com.dnx.bans.api.BanAPI;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnBan extends Command {

	public UnBan(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof ProxiedPlayer)) {
			if (args.length < 1) {
				sender.sendMessage("§cUso correto /unban <Player>");
				return;
			}
			ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
				public void run() {
					BanAPI.unban(args[0], null);
				}
			});
			return;
		}
		ProxiedPlayer by = (ProxiedPlayer) sender;
		if (!sender.hasPermission("dbans.unban")) {
			sender.sendMessage(Main.getInstance().configs.mensagemDeNaoTemPermissaoParaComando.replaceAll("&", "§"));
			return;
		}
		if (args.length < 1) {
			by.sendMessage("§cUso correto: /unban <Player>");
			return;
		}
		ProxyServer.getInstance().getScheduler().runAsync(Main.getInstance(), new Runnable() {
			public void run() {
				BanAPI.unban(args[0], by.getUniqueId());
			}
		});
	}

}
