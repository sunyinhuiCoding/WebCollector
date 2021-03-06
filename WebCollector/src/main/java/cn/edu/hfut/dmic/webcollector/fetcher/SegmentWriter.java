/*
 * Copyright (C) 2014 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.edu.hfut.dmic.webcollector.fetcher;

import cn.edu.hfut.dmic.webcollector.generator.DbWriter;
import cn.edu.hfut.dmic.webcollector.model.Content;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.parser.ParseData;
import cn.edu.hfut.dmic.webcollector.parser.ParseResult;
import cn.edu.hfut.dmic.webcollector.parser.ParseText;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hu
 */
public class SegmentWriter {

    public static synchronized String createSegmengName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String datestr = sdf.format(date);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return datestr;
    }

    private String segment_path;

    public SegmentWriter(String segment_path) {
        this.segment_path = segment_path;
        count_fetch = 0;
        count_content = 0;
        count_parse = 0;

        try {
            fetchWriter = new DbWriter<CrawlDatum>(CrawlDatum.class, segment_path + "/fetch");
            contentWriter = new DbWriter<Content>(Content.class, segment_path + "/content");
            parseDataWriter = new DbWriter<ParseData>(ParseData.class, segment_path + "/parse_data");
            parseTextWriter = new DbWriter<ParseText>(ParseText.class, segment_path + "/parse_text");
        } catch (IOException ex) {
            Logger.getLogger(SegmentWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DbWriter<CrawlDatum> fetchWriter;
    private DbWriter<Content> contentWriter;
    private DbWriter<ParseData> parseDataWriter;
    private DbWriter<ParseText> parseTextWriter;
    private int count_content;
    private int count_parse;
    private int count_fetch;

    public synchronized void wrtieFetch(CrawlDatum fetch) throws IOException {
        fetchWriter.write(fetch);
        count_fetch = (count_fetch++) % 50;
        if (count_fetch == 0) {
            fetchWriter.flush();
        }
    }

    public synchronized void wrtieContent(Content content) throws IOException {
        contentWriter.write(content);
        count_content = (count_content++) % 50;
        if (count_content == 0) {
            contentWriter.flush();
        }
    }

    public synchronized void wrtieParse(ParseResult parseresult) throws IOException {
        parseDataWriter.write(parseresult.parsedata);
        parseTextWriter.write(parseresult.parsetext);
        count_parse = (count_parse++) % 50;
        if (count_parse == 0) {
            parseDataWriter.flush();
            parseTextWriter.flush();
        }
    }

    public void close() throws IOException {
        fetchWriter.close();
        contentWriter.close();
        parseDataWriter.close();
        parseTextWriter.close();
    }

}
