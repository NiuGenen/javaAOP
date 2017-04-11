package ng.cmd;

import java.io.File;
import java.io.IOException;

/**
 * shell program
 * 1.read from user input
 * 2.get command , options and parameters
 * 3.dispatch command to its check parameter validity function
 * 4.execution function
 */
public class CmdShell implements IShell , IAopTest{
	
	/**
	 * command: test [string] [integer]
	 * test if AOP is working
	 */
	public Integer test_aop(String[] objs){
		FileSystem.io_write_to_console_line("This is a test command for aop.\n");
		assert(objs.length >= 3);
		String test = objs[0];
		String aop = objs[1];
		Integer number = Integer.parseInt(objs[2]);
		
		FileSystem.io_write_to_console_line("Input string: " + test);
		FileSystem.io_write_to_console_line("Input string:  " + aop);
		FileSystem.io_write_to_console_line("Input integer: " + number);
		
		return number + 1;
	}
	
	private String info_version = "My cmd shell 0.1";
	private String info_usage = "command [parameter] [option]\n" +
								"\n" +
								"cmd supported:\n" + 
								"\n" +
								"    pwd\n" + 
								"        print current working directory\n" +
								"    cd [dir]\n" +
								"        change current working directory" +
								"    ls [dir]\n" +
								"        list dir's files and directories\n" +
								"        options supported:\n" +
								"            -h : human readable\n" +
								"            -l : list detail information\n" +
								"    cat [filename]\n" +
								"        print file's content\n" +
								"        options supported:\n" +
								"            -l : with line number\n" +
								"            -s [num]: start at this line\n" +
								"            -e [num]: end at this line\n" +
								"    mkdir [dir]\n" +
								"        create directory\n" +
								"    touch [filename]\n" +
								"        create an empty file\n" +
								"    rmdir [dir]\n" +
								"        delete one directory\n" +
								"    rm [filename]\n" +
								"        delete one file\n" +
								"    exit\n" +
								"        exit shell";
	
	/*
	 * maintain current working directory
	 */
	private String current_working_directory = "C:\\\\";
	public String get_cwd(){
		return current_working_directory;
	}
	public void set_cwd(String pwd){
		current_working_directory = pwd;
	}
	
	/**
	 * command: pwd
	 * print current working directory
	 */
	public void cmd_pwd(){
		FileSystem.io_write_to_console_line( "CWD: " + get_cwd() );
	}
	
	/**
	 * command: cd [directory]
	 * change current working directory
	 * let Audit to check the validity of input string
	 * @throws IOException 
	 * @dir need one valid directory
	 */
	public void cmd_cd(String dir) throws IOException{
		if(FileSystem.if_path_absolute(dir))
			set_cwd( dir );
		else if(FileSystem.if_path_relative(dir))
			set_cwd( FileSystem.combine_path( get_cwd(), dir) );
		else
			FileSystem.io_write_to_stderror_line("connot resolve path: " + dir);
	}
	
	/**
	 * command: ls [directory...]
	 * @param dir
	 * @throws IOException 
	 */
	public void cmd_ls(String dir) throws IOException{
		
		File directory = FileSystem.get_file_by_path(get_cwd(), dir);
		if(directory == null){
			FileSystem.io_write_to_stderror_line("cannot resolve path : " + dir);
			return;
		}
		
		File[] subs = directory.listFiles();
		for(File f : subs){
			if(f.isFile()){
				FileSystem.io_write_to_console_line("[F] " + f.getName());
			}
			else if(f.isDirectory()){
				FileSystem.io_write_to_console_line("[D] " + f.getName());
			}
		}
	}
	
	/**
	 * print one file's content
	 */
	public void cmd_cat(String file) throws IOException{
		
		File f = FileSystem.get_file_by_path(get_cwd(), file);
		if( f == null ){
			FileSystem.io_write_to_stderror_line("cannot resolve file : " + file);
			return;
		}
		
		String content = FileSystem.io_read_from_file_all(f);
		
		FileSystem.io_write_to_console_line(content);
	}
	
	/**
	 * remove a file not a directory
	 */
	public boolean cmd_rm(String file){
		File f = new File(file);
		return f.delete();
	}
	
	/**
	 * exit the shell program. 
	 * using system.exit(0)
	 */
	public void cmd_exit(){
		System.exit(0);
	}
	
	public String get_version() {
		return info_version;
	}
	public String get_usage() {
		return info_usage;
	}
}
