package net.fusionlord.rpgloot;

import net.minecraftforge.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class Logger
{
	public static Logger INSTANCE;
	public Logger() {
		super();
		INSTANCE = this;
	}

	public void log(Level logLevel, Object msg)
	{
		FMLLog.log.log(logLevel, String.valueOf(msg));
	}

	public void warn(Object msg)
	{
		log(Level.WARN, msg);
	}

	public void info(Object msg)
	{
		log(Level.INFO, msg);
	}

	public void error(Object msg)
	{
		log(Level.ERROR, msg);
	}

	public static void slog(Level level, String s) {
		INSTANCE.log(level, s);
	}
}