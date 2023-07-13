package com.sds.hakli.viewmodel;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.A;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Foot;
import org.zkoss.zul.Footer;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.McabangDAO;
import com.sds.hakli.dao.MprovDAO;
import com.sds.hakli.dao.TanggotaDAO;
import com.sds.hakli.dao.TanggotarolesDAO;
import com.sds.hakli.domain.Anggota;
import com.sds.hakli.domain.Mcabang;
import com.sds.hakli.domain.Mprov;
import com.sds.hakli.domain.Tanggota;
import com.sds.hakli.domain.Tanggotaroles;
import com.sds.utils.AppData;
import com.sds.utils.AppUtils;
import com.sds.utils.db.StoreHibernateUtil;

public class BranchOrgVm {
	
	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();
	private Tanggota oUser;
	
	private TanggotaDAO anggotaDao = new TanggotaDAO();
	private TanggotarolesDAO oDao = new TanggotarolesDAO();
	
	private Mcabang objForm;
	private String hpKetua;
	private String hpSekretaris1;
	private String hpSekretaris2;
	private String hpBendahara1;
	private String hpBendahara2;
	
	@Wire
	private Window winBranchOrg;
	@Wire
	private Grid grid;
	@Wire
	private Image imgKetua;
	@Wire
	private Image imgSekretaris1;
	@Wire
	private Image imgSekretaris2;
	@Wire
	private Image imgBendahara1;
	@Wire
	private Image imgBendahara2;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view, @ExecutionArgParam("obj") Mcabang obj) {
		Selectors.wireComponents(view, this, false);
		
		oUser = (Tanggota) zkSession.getAttribute("anggota");
		objForm = obj;
		try {
			if (obj.getKetuaid() != null && obj.getKetuaid().trim().length() > 0) {
				Tanggota pengurus = anggotaDao.findByFilter("noanggota = '" + obj.getKetuaid() + "'");
				if (pengurus != null) {
					imgKetua.setSrc(AppUtils.PATH_PHOTO + "/" + pengurus.getPhotolink());
					hpKetua = pengurus.getHp();
				}
			}
			if (obj.getSekretaris1id() != null && obj.getSekretaris1id().trim().length() > 0) {
				Tanggota pengurus = anggotaDao.findByFilter("noanggota = '" + obj.getSekretaris1id() + "'");
				if (pengurus != null) {
					imgSekretaris1.setSrc(AppUtils.PATH_PHOTO + "/" + pengurus.getPhotolink());
					hpSekretaris1 = pengurus.getHp();
				}
			}
			if (obj.getSekretaris2id() != null && obj.getSekretaris2id().trim().length() > 0) {
				Tanggota pengurus = anggotaDao.findByFilter("noanggota = '" + obj.getSekretaris2id() + "'");
				if (pengurus != null) {
					imgSekretaris2.setSrc(AppUtils.PATH_PHOTO + "/" + pengurus.getPhotolink());
					hpSekretaris2 = pengurus.getHp();
				}
			}
			if (obj.getBendahara1id() != null && obj.getBendahara1id().trim().length() > 0) {
				Tanggota pengurus = anggotaDao.findByFilter("noanggota = '" + obj.getBendahara1id() + "'");
				if (pengurus != null) {
					imgBendahara1.setSrc(AppUtils.PATH_PHOTO + "/" + pengurus.getPhotolink());
					hpBendahara1 = pengurus.getHp();
				}
			}
			if (obj.getBendahara2id() != null && obj.getBendahara2id().trim().length() > 0) {
				Tanggota pengurus = anggotaDao.findByFilter("noanggota = '" + obj.getBendahara2id() + "'");
				if (pengurus != null) {
					imgBendahara2.setSrc(AppUtils.PATH_PHOTO + "/" + pengurus.getPhotolink());
					hpBendahara2 = pengurus.getHp();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		doRefresh();
	}
	
	@Command
	public void doRefresh() {
		try {
			List<Tanggotaroles> objList = oDao.listByFilter("tanggota.mcabang.mcabangpk = " + objForm.getMcabangpk() + " and tanggota.musergroup.usergroupcode = '" + AppUtils.ANGGOTA_ROLE_PENGURUSKABUPATEN + "'", "usergroupname, nama");
			int counter = 0;
			String group = null;
			for (Tanggotaroles role: objList) {
				if (group != null && !group.equals(role.getMusergroup().getUsergroupname()))
					counter = 0;
				
				Row row = new Row();
				row.setValign("top");
				
				Label lblRole = new Label(role.getMusergroup().getUsergroupname() + " " + (++counter));
				lblRole.setStyle("font-weight: bold");
				
				row.appendChild(lblRole);
				Hlayout hlayout = new Hlayout();
				Image img = new Image(AppUtils.PATH_PHOTO + "/" + role.getTanggota().getPhotolink());
				img.setWidth("75px");
				hlayout.appendChild(img);
				
				Grid gridPengurus = new Grid();
				Columns columns = new Columns();
				Column column1 = new Column();
				column1.setWidth("80px");
				Column column2 = new Column();
				columns.appendChild(column1);
				columns.appendChild(column2);
				gridPengurus.appendChild(columns);
				Rows rows = new Rows();
				Row rowNoanggota = new Row();
				rowNoanggota.appendChild(new Label("No Anggota"));
				rowNoanggota.appendChild(new Label(role.getTanggota().getNoanggota()));
				rows.appendChild(rowNoanggota);
				Row rowNama = new Row();
				rowNama.appendChild(new Label("Nama"));
				rowNama.appendChild(new Label(role.getTanggota().getNama()));
				rows.appendChild(rowNama);
				Row rowHp = new Row();
				rowHp.appendChild(new Label("No HP"));
				rowHp.appendChild(new Label(role.getTanggota().getHp()));
				rows.appendChild(rowHp);
				gridPengurus.appendChild(rows);
				
				Foot foot = new Foot();
				Footer footer = new Footer();
				footer.setSpan(2);
				Button btView = new Button("Detail");
				btView.setIconSclass("z-icon-eye");
				btView.setSclass("btn btn-primary btn-sm");
				btView.setAutodisable("self");
				btView.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doView(role.getTanggota().getNoanggota());
					}
				});
				footer.appendChild(btView);
				foot.appendChild(footer);
				gridPengurus.appendChild(foot);
				
				hlayout.appendChild(gridPengurus);
				
				row.appendChild(hlayout);
				grid.getRows().appendChild(row);
				
				group = role.getMusergroup().getUsergroupname();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doView(@BindingParam("noanggota") String noanggota) {
		if (noanggota != null && noanggota.trim().length() > 0) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("noanggota", noanggota);
			map.put("acttype", "view");
			Window win = (Window) Executions
					.createComponents("/view/anggota/anggotaedit.zul", null, map);
			win.setClosable(true);
			win.setWidth("98%");
			win.doModal();
		} else {
			Messagebox.show("Tidak ada data", WebApps.getCurrent().getAppName(), Messagebox.OK,
					Messagebox.INFORMATION);
		}
	}

	public Mcabang getObjForm() {
		return objForm;
	}

	public void setObjForm(Mcabang objForm) {
		this.objForm = objForm;
	}

	public String getHpKetua() {
		return hpKetua;
	}

	public void setHpKetua(String hpKetua) {
		this.hpKetua = hpKetua;
	}

	public String getHpSekretaris1() {
		return hpSekretaris1;
	}

	public void setHpSekretaris1(String hpSekretaris1) {
		this.hpSekretaris1 = hpSekretaris1;
	}

	public String getHpSekretaris2() {
		return hpSekretaris2;
	}

	public void setHpSekretaris2(String hpSekretaris2) {
		this.hpSekretaris2 = hpSekretaris2;
	}

	public String getHpBendahara1() {
		return hpBendahara1;
	}

	public void setHpBendahara1(String hpBendahara1) {
		this.hpBendahara1 = hpBendahara1;
	}

	public String getHpBendahara2() {
		return hpBendahara2;
	}

	public void setHpBendahara2(String hpBendahara2) {
		this.hpBendahara2 = hpBendahara2;
	}

}
