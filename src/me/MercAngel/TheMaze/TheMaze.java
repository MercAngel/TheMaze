package me.MercAngel.TheMaze;

import java.util.Random;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class TheMaze extends JavaPlugin {

	public static TheMaze plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	protected FileConfiguration config;
	Random ranGen = new Random(System.currentTimeMillis());
	public int mX, mY, ndir, zdir, wallid, floorid, roofid, nID, Mlv, MMlv;
	public boolean glow, roof, mazecreated;
	public int[][][] maze = new int[52][52][5];
	
	@Override
	public void onDisable() {
		config.set("wallid", wallid);
		config.set("floorid", floorid);
		config.set("roofid", roofid);
		config.set("levels", MMlv);
		config.set("glow", glow);
		config.set("roof", roof);
		saveConfig();
		PluginDescriptionFile ds = this.getDescription();
		this.logger.info(ds.getName() + " version " + ds.getVersion() + " is now disabled");
		
	}

	@Override
	public void onEnable() {
		config = getConfig();
		wallid=config.getInt("wallid",1);
		floorid=config.getInt("floorid",1);
		roofid=config.getInt("roofid",1);
		MMlv=config.getInt("levels",1);
		glow=config.getBoolean("glow",true);
		roof=config.getBoolean("roof",false);
		PluginDescriptionFile ds = this.getDescription();
		this.logger.info(ds.getName() + " version " + ds.getVersion() + " is now enabled");
	
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){		

		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (commandLabel.equalsIgnoreCase("maze")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				sender.sendMessage(ChatColor.RED + "[TheMaze] FloorID: " + ChatColor.WHITE + floorid);
				sender.sendMessage(ChatColor.RED + "[TheMaze] WallID: " + ChatColor.WHITE + wallid);
				sender.sendMessage(ChatColor.RED + "[TheMaze] RoofID: " + ChatColor.WHITE + roofid);
				sender.sendMessage(ChatColor.RED + "[TheMaze] GlowStone: " + ChatColor.WHITE + glow);
				sender.sendMessage(ChatColor.RED + "[TheMaze] Number Of Floors: " + ChatColor.WHITE + MMlv);
				sender.sendMessage(ChatColor.RED + "[TheMaze] Mob Spanwers : " + ChatColor.WHITE + "False (Not Working)");
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazecommand")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /maze: " + ChatColor.WHITE + "Show Settings");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazecreate : " + ChatColor.WHITE + "Create a random maze");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazeplace: " + ChatColor.WHITE + "Place Maze in world");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazeglow: " + ChatColor.WHITE + "Toggle GlowStone On/Off");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazeroof: " + ChatColor.WHITE + "Toggle Roof On/Off");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazelevel: " + ChatColor.WHITE + "Toggle number of floors from 1 to 2");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazeroofid id: " + ChatColor.WHITE + "Set The Block Type For Roof");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazewallid id: " + ChatColor.WHITE + "Set Block Type For Walls");
				sender.sendMessage(ChatColor.RED + "[TheMaze] Command /mazefloorid id: " + ChatColor.WHITE + "Set Block Type For Walls");
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazecreate")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				CreateMaze(player);				
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazeplace")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				PlaceMaze(player,1);
				if (MMlv >=2){
					PlaceMaze(player,2);
				}
				if (MMlv >=3){
					PlaceMaze(player,3);
				}
				if (MMlv ==4){
					PlaceMaze(player,4);
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazeglow")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				if (glow) {
					glow=false;
					sender.sendMessage(ChatColor.RED + "[Maze]" + ChatColor.WHITE + " Maze Glowstone is now off");
				} else {
					glow=true;
					sender.sendMessage(ChatColor.RED + "[Maze]" + ChatColor.WHITE + " Maze Glowstone is now on");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazeroof")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				if (roof) {
					roof=false;
					sender.sendMessage(ChatColor.RED + "[Maze]" + ChatColor.WHITE + " Maze Roof is now off");
				} else {
					roof=true;
					sender.sendMessage(ChatColor.RED + "[Maze]" + ChatColor.WHITE + " Maze Roof is now on");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazelevel")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				if (MMlv==1) {
					MMlv=2;
					sender.sendMessage(ChatColor.RED + "[Maze]" + ChatColor.WHITE + " Maze Number set to 2");
				} else {
					MMlv=1;
					sender.sendMessage(ChatColor.RED + "[Maze]" + ChatColor.WHITE + " Maze Number set to 1");
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazeroofid")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
					if (args.length==0){
						sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " usage: /mazeroofid ID(block type)");
						return false;
					}
					try {
						nID=Integer.parseInt(args[0]);
					}
					catch (NumberFormatException nfe) {
						sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " You must use a vaild integer");
						return false;
					}
					if (nID==7 || nID==89){
						sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " Your can not use this block type");
						return false;
					}
					roofid=nID;
					sender.sendMessage(ChatColor.RED + "Maze RoofID set to block type : " + ChatColor.WHITE + roofid);
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazewallid")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				if (args.length==0){
					sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " usage: /mazewallid ID(block type)");
					return false;
				}
				try {
					nID=Integer.parseInt(args[0]);
				}
				catch (NumberFormatException nfe) {
					sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " You must use a vaild integer");
					return false;
				}
				if (nID==7 || nID==89){
					sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " Your can not use this block type");
					return false;
				}
				wallid=nID;
				sender.sendMessage(ChatColor.RED + "Maze WallID set to block type : " + ChatColor.WHITE + wallid);
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		if (commandLabel.equalsIgnoreCase("mazefloorid")){
			if (player == null){
				sender.sendMessage("this command can only be run by a player");
				return false;
			}
			if (player.isOp()) {
				if (args.length==0){
					sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " usage: /mazefloorid ID(block type)");
					return false;
				}
				try {
					nID=Integer.parseInt(args[0]);
				}
				catch (NumberFormatException nfe) {
					sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " You must use a vaild integer");
					return false;
				}
				if (nID==7 || nID==89){
					sender.sendMessage(ChatColor.RED + "[Syntax error!]" + ChatColor.WHITE + " Your can not use this block type");
					return false;
				}
				floorid=nID;
				sender.sendMessage(ChatColor.RED + "Maze floorID set to block type : " + ChatColor.WHITE + floorid);
			} else {
				sender.sendMessage(ChatColor.RED + "[WARRNING]" + ChatColor.WHITE + " this command can only be run by an Op");
			}
		}
		return false;
	}

	public void CreateMaze(Player player) {
		int nend = 0;
		for (int i=1;i<52;i++){
			for (int ii=1;ii<52;ii++){
				maze[i][ii][1]=0;
				maze[i][ii][2]=0;
				maze[i][ii][3]=0;
				maze[i][ii][4]=0;
			}
		}
		Mlv=1;
		mX=24;
		mY=24;
		maze[mX][mY][Mlv]=13;
		player.sendMessage(ChatColor.RED + "NOTE:" + ChatColor.WHITE + "Createing Maze this make take a min or two");
		while (true){
			nend = 0;
			if (MMlv > 1) {
				ndir=ranGen.nextInt(6) +1;
			} else {
				ndir=ranGen.nextInt(4) +1;
			}
			zdir=ndir;
			int Chk = Mcheck();
			if (Chk == 0) {
				nend = maze[mX][mY][Mlv];
				if (nend == 11 || nend == 12) {
					maze[mX][mY][Mlv] = 14;
				} else {
					maze[mX][mY][Mlv] = 1;
				}
			}
			if (nend == 7){
				mX = mX + 2;
			}
			if (nend == 8 ){
				mY = mY -2;
			}
			if (nend == 9) {
				mX = mX -2;
			}
			if (nend == 10){
				mY = mY + 2;
			}
			if (nend == 11) {
				Mlv = Mlv - 1;
			}
			if (nend == 12) {
				Mlv = Mlv + 1;
			}
			if (nend == 13) {
				break;
			}
		}
		mazecreated=true;
		int x=(ranGen.nextInt(24) + 1) * 2 ;
		maze[1][x][1] = 1;
		x=(ranGen.nextInt(24) + 1) * 2 ;
		maze[51][x][1] = 1;
		player.sendMessage(ChatColor.RED + "NOTE:" + ChatColor.WHITE + "Done Createing Maze");
	}
	public void PlaceMaze(Player player, int fl) {
		if (mazecreated==false) {
			CreateMaze(player);
		}
		player.sendMessage(ChatColor.RED + "NOTE:" + ChatColor.WHITE + "Putting Maze Level: " + fl + " of " + MMlv + " in world this make take a min or two");
		double Z;
		Location pl = player.getLocation();
		World w = pl.getWorld();
		if (fl==1){
			pl.setY(pl.getY() - 1);
		}
		if (fl==2){
			pl.setY(pl.getY() + 3);
		}
		if (fl==3){
			pl.setY(pl.getY() + 6);
		}
		if (fl==4){
			pl.setY(pl.getY() + 9);
		}
		Block b = w.getBlockAt(pl);
		pl.setX(pl.getX() + 46);
		pl.setZ(pl.getZ() -46);
		Z = pl.getZ();
		for (int i=1;i<52;i++){
			pl.setZ(Z);
			for (int ii=1;ii<52;ii++){
				if (maze[i][ii][fl] == 0) {
					 b = w.getBlockAt(pl);
					 b.setTypeId(floorid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 if (roof==true && fl==MMlv){
						 b.setTypeId(roofid);
					 } else {
						 b.setTypeId(0);
					 }
					 pl.setY(pl.getY() - 4);
					 
					 pl.setX(pl.getX() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(floorid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 if (roof==true && fl==MMlv){
						 b.setTypeId(roofid);
					 } else {
						 b.setTypeId(0);
					 }
					 pl.setY(pl.getY() - 4);

					 pl.setZ(pl.getZ() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(floorid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 ndir=0;
					 if (glow){
						 ndir=ranGen.nextInt(10);						 
					 }
					 if (ndir==1){
						 b.setTypeId(89);
					 } else {
						 
						 b.setTypeId(wallid);
					 }
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 if (roof==true && fl==MMlv){
						 b.setTypeId(roofid);
					 } else {
						 b.setTypeId(0);
					 }
					 pl.setY(pl.getY() - 4);
					 
					 pl.setX(pl.getX() - 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(floorid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 b.setTypeId(wallid);
					 pl.setY(pl.getY() + 1);
					 b = w.getBlockAt(pl);
					 if (roof==true && fl==MMlv){
						 b.setTypeId(roofid);
					 } else {
						 b.setTypeId(0);
					 }
					 pl.setY(pl.getY() - 4);

					} else {
						 pl.setY(pl.getY() + 4);
						 b = w.getBlockAt(pl);
						 if (roof==true && fl==MMlv){
							 b.setTypeId(roofid);
						 } else {
							 b.setTypeId(0);
						 }
						 pl.setY(pl.getY() - 4);
						 b = w.getBlockAt(pl);
						 b.setTypeId(floorid);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() - 3);
						 
						 pl.setX(pl.getX() + 1);
						 pl.setY(pl.getY() + 4);
						 b = w.getBlockAt(pl);
						 if (roof==true && fl==MMlv){
							 b.setTypeId(roofid);
						 } else {
							 b.setTypeId(0);
						 }
						 pl.setY(pl.getY() - 4);
						 b = w.getBlockAt(pl);
						 b.setTypeId(floorid);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() - 3);
						 

						 pl.setZ(pl.getZ() + 1);
						 pl.setY(pl.getY() + 4);
						 b = w.getBlockAt(pl);
						 if (roof==true && fl==MMlv){
							 b.setTypeId(roofid);
						 } else {
							 b.setTypeId(0);
						 }
						 pl.setY(pl.getY() - 4);
						 b = w.getBlockAt(pl);
						 if (fl>=2) {
							 if (i>1) {
								 if ((maze[i][ii][fl] == 14) || (maze[i][ii][fl-1] == 14)) {
									 b.setTypeId(0);
									 pl.setX(pl.getX() + 1);
									 b = w.getBlockAt(pl);
									 b.getRelative(BlockFace.NORTH).setTypeIdAndData(65, (byte)0x4, false);
									 pl.setY(pl.getY() - 1);
									 b=w.getBlockAt(pl);
									 b.setTypeId(wallid);
									 b.getRelative(BlockFace.NORTH).setTypeIdAndData(65, (byte)0x4, false);
									 pl.setY(pl.getY() - 1);
									 b=w.getBlockAt(pl);
									 b.setTypeId(wallid);
									 b.getRelative(BlockFace.NORTH).setTypeIdAndData(65, (byte)0x4, false);
									 pl.setY(pl.getY() - 1);
									 b=w.getBlockAt(pl);
									 b.setTypeId(wallid);
									 b.getRelative(BlockFace.NORTH).setTypeIdAndData(65, (byte)0x4, false);
									 pl.setX(pl.getX() - 1);
									 pl.setY(pl.getY() + 3);
								 } else {
									 b.setTypeId(floorid);
								 }
							 } else {
								 b.setTypeId(floorid);
							 }
						 } else {
							 b.setTypeId(floorid);
						 }
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() - 3);
						 
						 pl.setX(pl.getX() - 1);
						 pl.setY(pl.getY() + 4);
						 b = w.getBlockAt(pl);
						 if (roof==true && fl==MMlv){
							 b.setTypeId(roofid);
						 } else {
							 b.setTypeId(0);
						 }
						 pl.setY(pl.getY() - 4);
						 b = w.getBlockAt(pl);
						 if (fl>=2) {
							 if (i==1) {
								 if ((maze[i][ii][fl] == 14) || (maze[i][ii][fl-1] == 14)) {
									 b.setTypeId(0);
								 } else {
									 b.setTypeId(floorid);
								 }
							 } else {
								 b.setTypeId(floorid);
							 }
						 } else {
							 b.setTypeId(floorid);
						 }
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() + 1);
						 b = w.getBlockAt(pl);
						 b.setTypeId(0);
						 pl.setY(pl.getY() - 3);
				}
				pl.setZ(pl.getZ() + 1);
			}
			pl.setX(pl.getX() -2);
		}
		pl = player.getLocation();
		w = pl.getWorld();
		pl.setY(pl.getY() - 1);
		b = w.getBlockAt(pl);
		b.setTypeId(7);
		player.sendMessage(ChatColor.RED + "NOTE:" + ChatColor.WHITE + "Done Putting Maze in world");
	}


	public int Mcheck() {
		int ret=1;
		int chk=0;
		int x1=0;
		int x2=0;
		int y1=1;
		int y2=2;
		int lv=0;
		while (true){
			chk=1;
			if (ndir == 1) {
				x1=-1;
				x2=-2;
				y1=0;
				y2=0;
				lv=0;
			}
			if (ndir == 2) {
				x1=0;
				x2=0;
				y1=1;
				y2=2;
				lv=0;
			}
			if (ndir == 3) {
				x1=1;
				x2=2;
				y1=0;
				y2=0;
				lv=0;
			}
			if (ndir == 4) {
				x1=0;
				x2=0;
				y1=-1;
				y2=-2;
				lv=0;
			}
			if (ndir == 5) {
				x1=0;
				x2=0;
				y1=0;
				y2=0;
				lv=1;
			}
			if (ndir == 6) {
				x1=0;
				x2=0;
				y1=0;
				y2=0;
				lv=-1;
			}
			if ((mX + x2 <=1) || (mX + x2 >=51) || (mY + y2 <=1) || (mY + y2 >=51) || (Mlv + lv <1) || (Mlv + lv > MMlv)) {
				chk= 0;
			}
			if ((chk == 1) && (maze[mX + x2][mY + y2][Mlv + lv]) != 0) {
				chk = 0;
			}
			if (chk == 0){
				ndir = ndir + 1;
				if (ndir == 7) {
					ndir=1;
				}
				if (ndir == zdir){
					ret = 0;
					break;
				}
			}
			if (chk == 1){
				if (lv !=0) {
					maze[mX + x2][mY + y2][Mlv + lv] = ndir + 6;
				} else {
					maze[mX + x2][mY + y2][Mlv] = ndir + 6;
					maze[mX + x1][mY + y1][Mlv] = 1;
				}
				mX = mX + x2;
				mY = mY + y2;
				Mlv = Mlv + lv;
				break;
			}
		}
		return ret;
	}
}