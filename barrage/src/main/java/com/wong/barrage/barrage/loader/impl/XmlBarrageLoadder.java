/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wong.barrage.barrage.BarrageEntity;

/**
 * 按 XML 格式来读取
 * @author 黄小天
 * @date 2019-03-19 20:22
 * @version 1.0
 */
class XmlBarrageLoadder extends AbstractBarrageLoader {
    
    private SAXReader reader = new SAXReader();
    
    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void load(int pageIndex, int pageSize, List<BarrageEntity> barrageList) {
        try {
            Document doc = reader.read(getPath().toFile());
            Element root = doc.getRootElement();
            List<Element> eleList = root.elements();
            for (Element element : eleList) {
                System.out.println(element);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) {
        XmlBarrageLoadder loadder = new XmlBarrageLoadder();
        loadder.setPath(Paths.get("dict/2.youdao.xml"));
        loadder.load(0, 100, new ArrayList<BarrageEntity>());
    }
}
