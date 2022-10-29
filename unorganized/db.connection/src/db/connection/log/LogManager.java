package db.connection.log;

import java.io.*;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LogManager {

	private static final String logfile;
	static {
            // ファイルパス
		logfile = null;
	}

	public static void out(Class<?> sourceClass, String str, Object... args) {
		out("[" + sourceClass.getSimpleName() + "] " + str, args);
	}

	public static void out(String str, Object ... args) {
		out(String.format(str, args));
	}

	public static void out(String str) {
		write(System.out, "[INFO] " + str);
	}

	public static void err(Class<?> sourceClass, String str, Object... args) {
		err("[" + sourceClass.getSimpleName() + "] " + str, args);
	}

	public static void err(String str, Object ... args) {
		err(String.format(str, args));
	}

	public static void err(String str) {
		write(System.err, "[ERROR] " + str);
	}

	public static void err(Throwable e) {
		String errorMessage = e.toString();

		if (e.getStackTrace() != null) {
			errorMessage += System.lineSeparator()
					+ (Arrays.stream(e.getStackTrace())
					.map(StackTraceElement::toString)
					.collect(Collectors.joining(System.lineSeparator())));
		}

		err(errorMessage);

		Throwable cause = e.getCause();
		if (cause != null) {
			err(cause);
		}
	}

	private static void write(PrintStream printStream, String str) {
		String text = LocalTime.now() + " " + str;
		printStream.println(text);
		writeLogFile(text);
	}

	private static void writeLogFile(String str) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(logfile, true),"UTF-8"));

			bw.write(str);
			bw.newLine();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally {
			try {
				if(bw != null) {
					bw.flush();
					bw.close();
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
