/**
 * Copyright 2009 Core Information Solutions LLC
 *
 * This file is part of Core Tooling.
 *
 * Core Tooling is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the 
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * Core Tooling is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License 
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along 
 * with Core Tooling.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package core.tooling;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.Calendar;

import core.tooling.logging.LogFactory;
import core.tooling.logging.Logger;

public class LineCounter
{
    private static class DirectoryFilter implements FileFilter
    {
        /*
         * (non-Javadoc)
         * 
         * @see FileFilter#accept(File)
         */
        public boolean accept(File pathname)
        {
            return pathname.isDirectory() && !pathname.getName().equals("CVS");
        }
    }

    private static class JavaFileFilter implements FilenameFilter
    {
        /*
         * (non-Javadoc)
         * 
         * @see FilenameFilter#accept(File, String)
         */
        public boolean accept(File dir, String name)
        {
            return name.toLowerCase().endsWith(".java") || name.toLowerCase().endsWith(".jsp");
        }

    }
    private static Logger log = LogFactory.getLogger(LineCounter.class);
    public static void main(String[] args)
    {
        args = new String[] 
        {
                "/home/worleyc/archive/core/commonapp-client/src",
                "/home/worleyc/archive/core/commonapp-server/src",
                "/home/worleyc/archive/core/data-model/src",
                "/home/worleyc/archive/core/object-mock/src",
                "/home/worleyc/archive/core/service/src",
                "/home/worleyc/archive/core/tooling/src",
                "/home/worleyc/archive/core/demo-sales-FX/src",
                "/home/worleyc/archive/core/demo-sales-gwt/src",
                "/home/worleyc/archive/core/sales-client/src",
                "/home/worleyc/archive/core/sales-server/src",
                "/home/worleyc/archive/core/etl/src",
        };
            
        boolean rowFormat = "-r".equals(args[0]);

        LineCounter lineCounter = new LineCounter(args);
        long[] total = new long[ARRAY_LENGTH];
        for (int i = rowFormat ? 1 : 0; i < args.length; i++)
        {
            long[] stats = new long[ARRAY_LENGTH];

            lineCounter.countLinesInDirectory(new File(args[i]), stats);
            if (rowFormat)
            {
                System.out.print(Calendar.getInstance().getTime());
                System.out.print(',');
                System.out.print(args[i]);
                System.out.print(',');
                lineCounter.printStatsAsRow(System.out, stats);
            }
            else
            {
                System.out.println(args[i]);
                lineCounter.printStatsAsColumn(System.out, stats);
            }
            System.out.println();
            total = lineCounter.addLongArrays(total, stats);
        }

        System.out.print(Calendar.getInstance().getTime());
        System.out.print(',');
        System.out.print("Total");
        if (rowFormat)
        {
            System.out.print(',');
            lineCounter.printStatsAsRow(System.out, total);
        }
        else
        {
            System.out.println();
            lineCounter.printStatsAsColumn(System.out, total);
        }
        System.out.println();
    }
    private String[] baseDirs;
    private static final int ARRAY_LENGTH = 5;
    private static final int CODE = 0;
    private static final int COMMENT = 1;

    private static final int IGNORED = 2;
    private static final int FILES = 3;

    private static final int TOTAL = 4;

    private JavaFileFilter javaFileFilter = new JavaFileFilter();

    private DirectoryFilter directoryFilter = new DirectoryFilter();

    public LineCounter(String[] baseDirs)
    {
        this.baseDirs = baseDirs;
    }

    public long[] addLongArrays(long[] array0, long[] array1)
    {
        long[] retval = new long[array0.length];
        for (int i = array0.length; --i >= 0;)
        {
            retval[i] = array0[i] + array1[i];
        }
        return retval;
    }

    public long[] countLines()
    {
        long[] stats = new long[ARRAY_LENGTH];
        for (int i = 0; i < baseDirs.length; i++)
        {
            countLinesInDirectory(new File(baseDirs[i]), stats);
        }
        return stats;
    }

    public void countLinesInDirectory(File directory, long[] stats)
    {
        File[] javaFiles = directory.listFiles(javaFileFilter);
        for (int i = javaFiles.length; --i >= 0;)
        {
            countLinesInFile(javaFiles[i], stats);
        }

        File[] subDirectories = directory.listFiles(directoryFilter);
        for (int i = subDirectories.length; --i >= 0;)
        {
            countLinesInDirectory(subDirectories[i], stats);
        }
    }

    public void countLinesInFile(File file, long[] stats)
    {
        try
        {
            LineNumberReader lnr = new LineNumberReader(new FileReader(file));
            stats[FILES]++;
            String line = null;
            boolean inBlockComment = false;
            while ((line = lnr.readLine()) != null)
            {
                stats[TOTAL]++;
                line = line.trim();
                if (line.length() >= 2 && line.charAt(0) == '/' && line.charAt(1) == '/')
                {
                    // System.out.println("comment: " + line);
                    stats[COMMENT]++;
                }
                else
                {
                    int blockCommentBeginIndex = -1;
                    int position = 0;
                    boolean wasInBlockComment = inBlockComment;

                    if (inBlockComment)
                    {
                        int blockCommentEndIndex = -1;
                        if ((blockCommentEndIndex = line.indexOf("*/", position)) != -1)
                        {
                            position = blockCommentEndIndex + 1;
                            inBlockComment = false;
                        }
                    }

                    while ((blockCommentBeginIndex = line.indexOf("/*", position)) != -1)
                    {
                        position = blockCommentBeginIndex + 1;
                        int blockCommentEndIndex = -1;
                        if ((blockCommentEndIndex = line.indexOf("*/", position)) != -1)
                        {
                            position = blockCommentEndIndex;
                            wasInBlockComment = true;
                            continue;
                        }
                        else
                        {
                            inBlockComment = true;
                        }
                    }

                    if (line.length() > 3
                            || (line.trim().equals("{") || line.trim().equals("}") || line.trim().equals("try")))
                    {
                        if (inBlockComment || wasInBlockComment)
                        {
                            // System.out.println("comment: " + line);
                            stats[COMMENT]++;
                        }
                        else
                        {
                            // System.out.println("code: " + line);
                            stats[CODE]++;
                        }
                    }
                    else
                    {
                        // System.out.println("ignored: " + line);
                        stats[IGNORED]++;
                    }
                }
            }
            lnr.close();
        }
        catch (IOException ioe)
        {
            log.error(ioe.getMessage(), ioe);
        }
    }

    public void printStatsAsColumn(PrintStream out, long[] stats)
    {
        out.print("Files            : ");
        out.println(stats[FILES]);
        out.print("Lines of Code    : ");
        out.println(stats[CODE]);
        out.print("Lines of Comments: ");
        out.println(stats[COMMENT]);
        out.print("Ignored Lines    : ");
        out.println(stats[IGNORED]);
        out.print("Total Lines      : ");
        out.println(stats[TOTAL]);
    }

    public void printStatsAsRow(PrintStream out, long[] stats)
    {
        out.print(stats[FILES]);
        out.print(',');
        out.print(stats[CODE]);
        out.print(',');
        out.print(stats[COMMENT]);
        out.print(',');
        out.print(stats[IGNORED]);
        out.print(',');
        out.print(stats[TOTAL]);
    }
}
