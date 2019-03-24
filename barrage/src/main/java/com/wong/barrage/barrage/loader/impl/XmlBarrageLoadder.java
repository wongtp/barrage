/*
 * Copyleft
 */
package com.wong.barrage.barrage.loader.impl;

import java.util.Collection;
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
    
    /** 1:有道单词词典 默认1**/
    private int type = 1;
    private SAXReader reader = new SAXReader();
    private Element root;
    
    @Override
    public void load(int pageIndex, int pageSize, Collection<BarrageEntity> barrageList) {
        switch (type) {
            case 1:
                parseYoudao(pageIndex, pageSize, barrageList);
                break;
            default:
                parseYoudao(pageIndex, pageSize, barrageList);
                break;
        }
    }
    
    private void parseYoudao(int pageIndex, int pageSize, Collection<BarrageEntity> barrageList) {
        System.out.println("===== 解析有道翻译数据");
        // 读取金山词霸数据
        List<Element> itemList = root.elements();
        int loopSize = pageSize;
        if (itemList.size() - pageIndex < pageSize) {
            loopSize = itemList.size();
        }
        for (int i = pageIndex; i < loopSize; i++) {
            Element item = itemList.get(i);
            String word = item.elementTextTrim("word");
            if (StringUtil.isEmpty(word)) {
                continue;
            }
            String trans = item.elementTextTrim("trans");
            barrageList.add(new BarrageEntity(word + " " + trans));
        }
    }

    @Override
    boolean parse() {
        try {
            Document doc = reader.read(getPath().toFile());
            root = doc.getRootElement();
            String rootName = root.getName();
            // 有道的词典
            if ("wordbook".equals(rootName)) {
                type = 1;
                setSize(root.elements().size());
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return true;
    }
}
