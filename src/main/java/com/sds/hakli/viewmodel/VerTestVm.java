package com.sds.hakli.viewmodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.Tp2kbA01DAO;
import com.sds.hakli.domain.Tp2kb;
import com.sds.hakli.domain.Tp2kba01;

public class VerTestVm {
	
	@Wire
	private Div divContent;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Tp2kb obj) {
		Selectors.wireComponents(view, this, false);
		try {
			String filter = "mp2kbkegiatan.mp2kbkegiatanpk = " + obj.getMp2kbkegiatan().getMp2kbkegiatanpk() + " and status = 'WC'";
			if (obj.getMp2kbkegiatan().getIdkegiatan().equals("A01")) {
				List<Tp2kba01> objList = new Tp2kbA01DAO().listByFilter(filter, "createtime");
				for (Tp2kba01 data: objList) {
					Div div = new Div();
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("obj", data);
					map.put("isApprove", "Y");
					Executions.createComponents("/view/p2kb/p2kb"
							+ data.getMp2kbkegiatan().getIdkegiatan().trim().toLowerCase() + "detail.zul", div,
							map);
					div.setAttribute("obj", data);
					divContent.appendChild(div);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
