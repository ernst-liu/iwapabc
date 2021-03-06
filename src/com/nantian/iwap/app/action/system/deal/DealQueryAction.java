package com.nantian.iwap.app.action.system.deal;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nantian.iwap.biz.actions.TransactionBizAction;
import com.nantian.iwap.biz.flow.BizActionException;
import com.nantian.iwap.common.util.StringUtil;
import com.nantian.iwap.databus.DTBHelper;
import com.nantian.iwap.persistence.DBAccessBean;
import com.nantian.iwap.persistence.DBAccessPool;
import com.nantian.iwap.persistence.DataObject;
import com.nantian.iwap.persistence.PaginationSupport;

public class DealQueryAction extends TransactionBizAction {
	private static Logger logger = Logger.getLogger(DealQueryAction.class);
	private String encryptClazz = "com.nantian.iwap.app.util.DefaultEncrypt";// 默认加密方式

	public int actionExecute(DTBHelper dtbHelper) throws BizActionException {
		String option = dtbHelper.getStringValue("option");
		if (StringUtil.isBlank(option)) {
			return query(dtbHelper);
		}
		if ("add".equals(option)) {
			return add(dtbHelper);
		}
		
		if ("save".equals(option)) {
			return save(dtbHelper);
		}
		if ("remove".equals(option)) {
			return remove(dtbHelper);
		}
		if ("show".equals(option)) {
			return show(dtbHelper);
		}
		if ("query".equals(option)) {
			return query(dtbHelper);
		}
		return 0;
	}

	protected int query(DTBHelper dtbHelper) {
		logger.info("-------query-------"+dtbHelper);
		int flag = 0;
		DBAccessBean dbBean = null;
		int start = Integer.valueOf(dtbHelper.getStringValue("start"));
		int limit = Integer.valueOf(dtbHelper.getStringValue("limit"));
		String fuzzySearch = "%" + dtbHelper.getStringValue("fuzzySearch") + "%";
		logger.info("fuzzySearch:"+fuzzySearch);
		try {
			dbBean = DBAccessPool.getDbBean();
			PaginationSupport page = new PaginationSupport(start, limit, limit);
			String sqlStr = "select a.disposalno,a.disposaltitle,"
							+ "to_char(disposaldatetime,'yyyy-mm-dd') disposaldatetime,"
							+ "a.disposalmodelabel,a.createpsnname,"
							+ "b.sactivitynames,b.sexecutornames,b.sstatusname "
							+ "from oa_asset_disposal_parent a,sa_task b "
							+ "where 1=1 "
							+ "and a.fid = b.sData1 and b.sKindID = 'tkProcessInstance' "
							+ "and (a.disposalno like ? or a.disposaltitle like ? or a.createpsnname like ? or a.disposalmodelabel like ? "
							+ "or b.sactivitynames like ? or b.sexecutornames like ? or b.sstatusname like ?) ";
			logger.info("执行sql语句:" + sqlStr);
			List<Map<String, Object>> dataList = dbBean.queryForList(sqlStr,page,fuzzySearch,fuzzySearch,fuzzySearch,fuzzySearch,fuzzySearch,fuzzySearch,fuzzySearch);
			logger.info("-----------dataList:" + dataList);
			if (dataList.size() > 0) {
				dtbHelper.setRstData("rows", dataList);
				dtbHelper.setRstData("total", page.getTotalCount());
			} else {
				dtbHelper.setError("depmg-err-s", "[数据库显示失败!]");
				DBAccessPool.releaseDbBean();
				return 0;
			}
			flag = 1;
		} catch (Exception e) {
			logger.error("维修列表查询出错", e);
			try {
				if (dbBean != null) {
					dbBean.executeRollBack();
				}
			} catch (Exception e2) {
				logger.error("数据库回滚出错", e2);
			}
		}
		return flag;
	}

	protected int add(DTBHelper dtbHelper) {
		
		return 0;
	}

	protected int save(DTBHelper dtbHelper) {

		return 0;
	}

	protected int remove(DTBHelper dtbHelper) {

		return 0;
	}

	public int show(DTBHelper dtbHelper) throws BizActionException {
		
		return 0;
	}

	protected int other(DTBHelper dtbHelper) throws BizActionException {

		return 0;
	}
}
