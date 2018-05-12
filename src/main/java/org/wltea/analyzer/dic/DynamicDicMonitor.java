package org.wltea.analyzer.dic;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.wltea.analyzer.cfg.Configuration;

import java.io.File;

/**
 * Created by qingyuanxue@sohu-inc.com.
 */

public class DynamicDicMonitor implements Runnable {

    public static ESLogger logger = Loggers.getLogger("ik-analyzer");

    /*
     * 上次更改时间
     */
    private long last_modified = 0L;

    Configuration cfg = null;

    /*
     * 地址
     */
    private String location;

    public DynamicDicMonitor(Configuration cfg, String location) {
        this.location = location;
        this.cfg = cfg;
    }

    /**
     * 监控流程：
     * 监控流程：
     * ①周期性查询动态词典文件，判断是否变化
     * ②如果未变化，休眠1min，返回第①步
     * ③如果有变化，重新加载词典
     * ④休眠1min，返回第①步
     */

    public void run() {
        try {
            File file = new File(this.cfg.getConfigInPluginDir().toString() + "/", this.location);
            logger.info(this.cfg.getConfigInPluginDir().toString() + "/" + this.location, new Object[0]);
            if (file.lastModified() != this.last_modified) {
                logger.info("the dynamic dic (" + this.location + ")has new word.", new Object[0]);
                Dictionary.getSingleton().reLoadMainDict();
                this.last_modified = file.lastModified();
            }
        } catch (Exception e) {
            Dictionary.logger.error("load dynamic dict {} error!", e, new Object[]{this.location});
        }
    }
}