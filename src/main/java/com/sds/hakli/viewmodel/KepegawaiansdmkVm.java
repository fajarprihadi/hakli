package com.sds.hakli.viewmodel;

import java.util.List;
import java.util.TimeZone;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.Validator;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WebApps;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Window;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sds.hakli.domain.Kepegawaiansdmk;
import com.sds.hakli.domain.GenericResp;
import com.sds.hakli.domain.Kepegawaian;
import com.sds.hakli.extension.ExtensionServices;
import com.sds.utils.config.ConfigUtil;

public class KepegawaiansdmkVm {
	
	private Kepegawaiansdmk objForm;
	private List<KepegawaiansdmkVm> objList;
	private String nama;
	private Integer totalrecords;
	private boolean isInsert;
	private ObjectMapper mapper = new ObjectMapper();
	private ListModelList<Kepegawaian> kepegawaianModel;
	
	@Wire
	private Window winKepegawaiansdmk;
	@Wire
	private Grid grid;
	@Wire
	private Button btAdd;
	@Wire
	private Button btSave;
	@Wire
	private Div divForm;
	@Wire
	private Combobox cbKepegawaian;
	
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		grid.setRowRenderer(new RowRenderer<Kepegawaiansdmk>() {

			@Override
			public void render(Row row, Kepegawaiansdmk data, int index) throws Exception {
				row.getChildren().add(new Label(String.valueOf(index+1)));
				row.getChildren().add(new Label(data.getMkepegawaian().getKepegawaian()));
				row.getChildren().add(new Label(data.getKepegawaiansdmk()));
				Button btEdit = new Button();
				btEdit.setIconSclass("z-icon-edit");
				btEdit.setSclass("btn btn-primary btn-sm");
				btEdit.setAutodisable("self");
				btEdit.setTooltiptext("Edit");
				btEdit.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						doAdd(data);
						BindUtils.postNotifyChange(KepegawaiansdmkVm.this, "objForm");
					}
				});
				row.getChildren().add(btEdit);
				
				Button btDel = new Button();
				btDel.setIconSclass("z-icon-trash-o");
				btDel.setSclass("btn btn-danger btn-sm");
				btDel.setAutodisable("self");
				btDel.setTooltiptext("Hapus");
				
				btDel.addEventListener(Events.ON_CLICK, new EventListener<Event>() {

					@Override
					public void onEvent(Event event) throws Exception {
						Messagebox.show("Anda ingin menghapus data ini?", "Confirm Dialog", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, new EventListener<Event>() {

							@Override
							public void onEvent(Event event)
									throws Exception {
								if (event.getName().equals("onOK")) {
									try {
										String url = ConfigUtil.getConfig().getUrl_base() + ConfigUtil.getConfig().getEndpoint_kepegawaiansdmk() + "/" + data.getId();
										GenericResp respAll = ExtensionServices.getResource(url, null, "delete", null);
										if (respAll.getCode() == 200) {
											Clients.showNotification("Proses hapus data berhasil", "info", null, "middle_center", 1500);
											doRefresh();
											BindUtils.postNotifyChange(KepegawaiansdmkVm.this, "*");
										} else {
											Messagebox.show(respAll.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
										}
									} catch (Exception e) {	
										Messagebox.show(e.getMessage(), WebApps.getCurrent().getAppName(), Messagebox.OK, Messagebox.ERROR);
										e.printStackTrace();
									}																													
								} 									
							}
							
						});
					}
				});
				
				Div div = new Div();
				div.appendChild(btEdit);
				div.appendChild(new Separator("vertical"));
				div.appendChild(btDel);
				row.getChildren().add(div);
			}
		});
		
		doReset();
		
		try {
			String url = ConfigUtil.getConfig().getUrl_base() + ConfigUtil.getConfig().getEndpoint_kepegawaian();
			GenericResp respAll = ExtensionServices.getResource(url, null, "get", null);
			if (respAll.getCode() == 200) {
				List<Kepegawaian> kepegawaians = mapper.readValue(mapper.writeValueAsString(respAll.getData()),
						new TypeReference<List<Kepegawaian>>() {
						});
				kepegawaianModel = new ListModelList<>(kepegawaians);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	public void doReset() {
		nama = null;
		doRefresh();
		divForm.setVisible(false);
		btAdd.setLabel("Tambah Kepegawaian SDMK");
		btAdd.setIconSclass("z-icon-plus-square");
		cbKepegawaian.setValue(null);
	}
	
	@Command
	@NotifyChange("totalrecords")
	public void doRefresh() {
		try {
			String url = ConfigUtil.getConfig().getUrl_base()
					+ ConfigUtil.getConfig().getEndpoint_kepegawaiansdmk();
			//JSONObject jsonReq = new JSONObject();
			GenericResp respAll = ExtensionServices.getResource(url, null,
					"get", null);

			if (respAll.getCode() == 200) {
				objList = mapper.readValue(mapper.writeValueAsString(respAll.getData()),
						new TypeReference<List<Kepegawaiansdmk>>() {
						});
				totalrecords = objList.size();
				grid.setModel(new ListModelList<>(objList) );
			} else {
				Messagebox.show("Terjadi kegagalan proses load data", WebApps.getCurrent().getAppName(), Messagebox.OK,
						Messagebox.ERROR);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Command
	@NotifyChange("*")
	public void doAdd(Kepegawaiansdmk obj) {
		if (obj != null) {
			isInsert = false;
			objForm = obj;
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Perbarui");
			
			cbKepegawaian.setValue(objForm.getMkepegawaian().getKepegawaian());
		} else if (btAdd.getLabel().equals("Tambah Kepegawaian SDMK")) {
			isInsert = true;
			objForm = new Kepegawaiansdmk();
			divForm.setVisible(true);
			btAdd.setLabel("Cancel");
			btAdd.setIconSclass("z-icon-reply");
			btSave.setLabel("Submit");
			
			cbKepegawaian.setValue(null);
		} else {
			divForm.setVisible(false);
			btAdd.setLabel("Tambah Kepegawaian SDMK");
			btAdd.setIconSclass("z-icon-plus-square");
		}
	}
	
	@Command
	@NotifyChange("objForm")
	public void doSave() {
		try {
			String url = ConfigUtil.getConfig().getUrl_base() + ConfigUtil.getConfig().getEndpoint_kepegawaiansdmk();
			mapper.setTimeZone(TimeZone.getDefault());
			mapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
			GenericResp respAll = ExtensionServices.getResource(url, mapper.writeValueAsString(objForm), isInsert ? "post" : "put", null);
			
			if (isInsert && respAll.getCode() == 201) {
				Kepegawaiansdmk data = (Kepegawaiansdmk) mapper.readValue(mapper.writeValueAsString(respAll.getData()),
						Kepegawaiansdmk.class);

				Clients.showNotification("Proses simpan data berhasil", "info", null, "middle_center", 1500);
				doReset();
			} else if (!isInsert && respAll.getCode() == 200) { 
				Clients.showNotification("Proses pembaruan data berhasil", "info", null, "middle_center", 1500);
				doReset();
			} else {
				Messagebox.show("Terjadi kegagalan proses simpan data", WebApps.getCurrent().getAppName(),
						Messagebox.OK, Messagebox.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Validator getValidator() {
		return new AbstractValidator() {

			@Override
			public void validate(ValidationContext ctx) {
				String kepegawaiansdmk = (String) ctx.getProperties("kepegawaiansdmk")[0].getValue();
				
				if (kepegawaiansdmk == null || "".equals(kepegawaiansdmk.trim()))
					this.addInvalidMessage(ctx, "kepegawaiansdmk", Labels.getLabel("common.validator.empty"));
				if (cbKepegawaian.getValue() == null || cbKepegawaian.getValue().trim().length() == 0)
					this.addInvalidMessage(ctx, "region", Labels.getLabel("common.validator.empty"));
				
				
			}
		};
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Integer getTotalrecords() {
		return totalrecords;
	}

	public void setTotalrecords(Integer totalrecords) {
		this.totalrecords = totalrecords;
	}

	public Kepegawaiansdmk getObjForm() {
		return objForm;
	}

	public void setObjForm(Kepegawaiansdmk objForm) {
		this.objForm = objForm;
	}

	public ListModelList<Kepegawaian> getKepegawaianModel() {
		return kepegawaianModel;
	}

	public void setKepegawaianModel(ListModelList<Kepegawaian> kepegawaianModel) {
		this.kepegawaianModel = kepegawaianModel;
	}

	

}
