/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.edu.hfut.dmic.webcollector.generator.filter;

import cn.edu.hfut.dmic.webcollector.generator.Generator;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import java.util.HashSet;

/**
 *
 * @author hu
 */
public class UniqueFilter extends Filter{

    public HashSet<String> hashset=new HashSet<String>();
    
    public void addUrl(String url){
         hashset.add(url);
    }

   
      
        
     
  
    
    public UniqueFilter(Generator generator) {
        super(generator);
    }

    @Override
    public CrawlDatum next() {
        while(true){
        CrawlDatum crawldatum=generator.next();
        if(crawldatum==null){
            return null;
        }
        String url=crawldatum.getUrl();
        if(hashset.contains(url)){
            continue;
        }
        else{
            addUrl(url);
            return crawldatum;
        }
        }
    }
    
}
