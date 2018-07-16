package com.dnx.bans.api;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.dnx.bans.Main;
import com.dnx.bans.mysql.MySQLManager;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MuteAPI {

	public static String message(ProxiedPlayer p) {
		String msg = Main.getInstance().getConfigs().mensagemDeMute.replaceAll("%por%", getBy(p.getName())).replaceAll("%razao%", getReason(p.getName()).replaceAll("&", "§"));
		if(whenExpires(p.getName()) == -1) {
			msg = msg + ". " + Main.getInstance().getConfigs().mensagemDeMutePermanente.replaceAll("&", "§");
		}else{
			msg = msg + ". " + Main.getInstance().getConfigs().mensagemDeMuteTemporario.replaceAll("&", "§").replaceAll("%tempo%", getHoras(getWhen(p.getName())-System.currentTimeMillis()));
		}
		return msg;
		
	}
	
	public static void unMuteBukkit(String playername) {
		if (playerJaMutadoOffline(playername)) {
			try {
				MySQLManager.db.updateSQL("DELETE FROM mute WHERE username='" + playername.toLowerCase() + "';");
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public static void unMute(String playername, UUID by) {
		try {
			if (by != null) {
				ProxyServer.getInstance().getPlayer(by).sendMessage("§aProcessando pedido!");
			} else {
				ProxyServer.getInstance().getConsole().sendMessage("§aProcessando pedido!");
			}
			playername = playername.toLowerCase();
			if (playerJaMutadoOffline(playername)) {
				MySQLManager.db.updateSQL("DELETE FROM mute WHERE username='" + playername.toLowerCase() + "';");
				if (by != null) {
					ProxyServer.getInstance().getPlayer(by).sendMessage("§aPlayer desmutado!");
				} else {
					ProxyServer.getInstance().getConsole().sendMessage("§aPlayer desmutado!");
				}
			} else {
				if (by != null) {
					ProxyServer.getInstance().getPlayer(by).sendMessage("§cPlayer nao esta mutado!!");
				} else {
					ProxyServer.getInstance().getConsole().sendMessage("§cPlayer nao esta mutado!");
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao desbanir o jogador");
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public static boolean banOffline(String playername, UUID by, String byname, String reason, long time) {
		try {
			if (by != null) {
				ProxyServer.getInstance().getPlayer(by).sendMessage("§aProcessando pedido!");
			} else {
				ProxyServer.getInstance().getConsole().sendMessage("§aProcessando pedido!");
			}
			boolean logou = MySQLManager.playerJaLogou(playername.toLowerCase());
			if (logou) {
				String uuid = MySQLManager.getUUID(playername);
				if (playerJaMutadoOffline(playername)) {
					if (by != null) {
						ProxyServer.getInstance().getPlayer(by).sendMessage("§cO jogador ja esta mutado!");
						return false;
					} else {
						ProxyServer.getInstance().getConsole().sendMessage("§cO jogador ja esta mutado!");
						return false;
					}
				} else {
					PreparedStatement stm = MySQLManager.db.getConnection().prepareStatement(
							"INSERT INTO `mute`(`user`, `username`, `byplayer`, `reason`, `time`, `mutedin`) VALUES (?, ?, ?, ?, ?, ?);");
					stm.setString(1, uuid);
					stm.setString(2, playername.toLowerCase());
					if (by == null) {
						stm.setString(3, "CONSOLE");
					} else {
						stm.setString(3, by.toString());
					}
					stm.setString(4, reason);
					stm.setLong(5, time);
					stm.setLong(6, System.currentTimeMillis());
					stm.executeUpdate();
					if (by != null) {
						ProxyServer.getInstance().getPlayer(by).sendMessage("§aPlayer mutado!");
						return true;
					} else {
						ProxyServer.getInstance().getConsole().sendMessage("§aPlayer mutado!");
						return true;
					}
				}
			} else {
				if (by != null) {
					ProxyServer.getInstance().getPlayer(by).sendMessage("§cO jogador nunca entrou no servidor!");
					return false;
				} else {
					ProxyServer.getInstance().getConsole().sendMessage("§cO jogador nunca entrou no servidor!");
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao banir o jogador " + playername);
		}
		return false;
	}
	
	public static long whenExpires(String playername) {
		playername = playername.toLowerCase();
		long when = 0;
		ResultSet rs;
		try {
			rs = MySQLManager.db.querySQL("SELECT * FROM mute WHERE username= '" + playername.toLowerCase() + "'");
			if (rs.next()) {
				when = rs.getLong("time");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return when;
	}

	public static String getReason(String playername) {
		playername = playername.toLowerCase();
		String reason;
		try {
			ResultSet rs = MySQLManager.db
					.querySQL("SELECT * FROM mute WHERE username= '" + playername.toLowerCase() + "'");
			if (rs.next()) {
				reason = rs.getString("reason");
			} else {
				reason = "NOT FOUND";
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			reason = "ERROR";
		}
		return reason;
	}

	public static long getWhen(String playername) {
		long when = 0;
		ResultSet rs;
		try {
			rs = MySQLManager.db.querySQL("SELECT * FROM mute WHERE username= '" + playername.toLowerCase() + "'");
			if (rs.next()) {
				when = rs.getLong("bannedin");
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return when;
	}

	public static String getBy(String playername) {
		playername = playername.toLowerCase();
		String nome = null;
		try {
			ResultSet rs = MySQLManager.db
					.querySQL("SELECT * FROM mute WHERE username= '" + playername.toLowerCase() + "'");
			if (rs.next()) {
				nome = rs.getString("byplayer");
			} else {
				nome = "Not found";
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return nome;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean banOnline(UUID player, String playername, UUID by, String byname, String reason, long time) {
		try {
			if (playerJaMutadoOnline(player)) {
				if (by != null) {
					ProxyServer.getInstance().getPlayer(by).sendMessage("§cO jogador ja esta mutado!");
					return false;
				} else {
					ProxyServer.getInstance().getConsole().sendMessage("§cO jogador ja esta mutado!");
					return false;
				}
			} else {
				if (by != null) {
					ProxyServer.getInstance().getPlayer(by).sendMessage("§aProcessando pedido!");
				} else {
					ProxyServer.getInstance().getConsole().sendMessage("§aProcessando pedido!");
				}
				PreparedStatement stm = MySQLManager.db.getConnection().prepareStatement(
						"INSERT INTO `mute`(`user`, `username`, `byplayer`, `reason`, `time`, `mutedin`) VALUES (?, ?, ?, ?, ?, ?);");
				stm.setString(1, player.toString());
				stm.setString(2, playername.toLowerCase());
				if (by == null) {
					stm.setString(3, "CONSOLE");
				} else {
					stm.setString(3, by.toString());
				}
				stm.setString(4, reason);
				stm.setLong(5, time);
				stm.setLong(6, System.currentTimeMillis());
				stm.executeUpdate();
				ProxyServer.getInstance().getPlayer(playername).sendMessage(message(ProxyServer.getInstance().getPlayer(playername)));
				if (by != null) {
					ProxyServer.getInstance().getPlayer(by).sendMessage("§cPlayer mutado!");
					return true;
				} else {
					ProxyServer.getInstance().getConsole().sendMessage("§cPlayer mutado!");
					return true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao mutar o jogador " + playername);
		}
		return false;
	}
	
	public static String getHoras(long time) {
		int h = (int) ((time / 1000) / 3600);
		int m = (int) (((time / 1000) / 60) % 60);
		int s = (int) ((time / 1000) % 60);
		return h+":"+m+":"+s;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean playerJaMutadoOnline(UUID player) {
		try {
			ResultSet rs = MySQLManager.db.querySQL("SELECT * FROM mute WHERE user= '" + player.toString() + "'");
			if (rs.next()) {
				return rs.getString("user") != null;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao verificar o jogador " + player);
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static boolean playerJaMutadoOffline(String player) {
		try {
			ResultSet rs = MySQLManager.db.querySQL("SELECT * FROM mute WHERE username= '" + player.toLowerCase() + "'");
			if (rs.next()) {
				return rs.getString("username") != null;
			}
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			ProxyServer.getInstance().getConsole().sendMessage("§cUm erro aconteceu ao verificar o jogador " + player);
		}
		return false;
	}
	
	public static String getHorasm(long time) {
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time);
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
	    long hours = TimeUnit.SECONDS.toHours(seconds) -
	                 TimeUnit.DAYS.toHours(day);
	    long minutes = TimeUnit.SECONDS.toMinutes(seconds) -
	                  TimeUnit.HOURS.toMinutes(TimeUnit.SECONDS.toHours(seconds));
	    long second = TimeUnit.SECONDS.toSeconds(seconds) -
	                  TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(seconds));

		StringBuilder sb = new StringBuilder();
		if (day > 0L) {
			sb.append(day).append(" ").append("days").append(" ");
		}
		if (hours > 0L) {
			sb.append(hours).append(" ").append("hours").append(" ");
		}
		if (minutes > 0L) {
			sb.append(minutes).append(" ").append("minutes").append(" ");
		}
		if (second > 0L) {
			sb.append(second).append(" ").append("seconds");
		}
		String diff = sb.toString();

		return diff.isEmpty() ? "0 " + "seconds" : diff;
	}

}