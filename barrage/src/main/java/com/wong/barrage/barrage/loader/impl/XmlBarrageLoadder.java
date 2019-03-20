/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.wong.barrage.barrage.BarrageEntity;
import com.wong.barrage.util.StringUtil;

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
            String rootName = root.getName();
            // 有道的词典
            if ("wordbook".equals(rootName)) {
                parseYoudao(pageIndex, pageSize, barrageList, root);
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
    
    private void parseYoudao(int pageIndex, int pageSize, List<BarrageEntity> barrageList, Element root) {
        List<Element> itemList = root.elements();
        int loopSize = pageSize;
        if (itemList.size() - pageIndex < pageSize) {
            loopSize = itemList.size();
        }
        for (int i = pageIndex; i < loopSize; i++) {
            Element item = itemList.get(i);
            String word = item.elementTextTrim("word");
            String trans = item.elementTextTrim("trans");
            if (StringUtil.isEmpty(word)) {
                continue;
            }
            System.out.println(word + " " + trans);
            BarrageEntity entity = new BarrageEntity(word + " " + trans);
            barrageList.add(entity);
        }
    }
}
