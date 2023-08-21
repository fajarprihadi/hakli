package com.sds.hakli.viewmodel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Window;

import com.sds.hakli.domain.Tanggota;

public class JasperViewerVm {

	private org.zkoss.zk.ui.Session zkSession = Sessions.getCurrent();

	private String reportPath;
	private String reportPath2;
	private Map<String, Object> parameters = new HashMap<>();
	private List<Tanggota> objList = new ArrayList<Tanggota>();
	private List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();

	@Wire
	private Div div;
	@Wire
	private Window window;

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
	@NotifyChange("*")
	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);

		try {
			this.reportPath = (String) zkSession.getAttribute("reportPath");
			this.parameters = (Map) zkSession.getAttribute("parameters");
			this.objList = (List<Tanggota>) zkSession.getAttribute("objList");
			this.reportPath2 = (String) zkSession.getAttribute("reportPath2");

			ByteArrayOutputStream output = new ByteArrayOutputStream();

			//System.out.println(reportPath);
			//System.out.println(reportPath2);

			JasperPrint jasperPrint = JasperFillManager.fillReport(reportPath, parameters,
					new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource((java.util.Collection) objList));
			jasperPrintList.add(jasperPrint);

			if (reportPath2 != null && reportPath2.trim().length() > 0) {
				JasperPrint jasperPrint2 = JasperFillManager.fillReport(reportPath2, parameters,
						new net.sf.jasperreports.engine.data.JRBeanCollectionDataSource(
								(java.util.Collection) objList));
				jasperPrintList.add(jasperPrint2);
			}
			
			//System.out.println("TOTAL PAGE : " + jasperPrintList.size());

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRPdfExporterParameter.IS_CREATING_BATCH_MODE_BOOKMARKS, Boolean.TRUE);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, output);
			exporter.exportReport();

			AMedia amedia = new AMedia("report", "pdf", "application/pdf", output.toByteArray());
			Iframe iframe = new Iframe();
			iframe.setHeight("100%");
			iframe.setWidth("100%");
			iframe.setContent(amedia);
			div.appendChild(iframe);

			if (zkSession.getAttribute("objList") != null)
				zkSession.removeAttribute("objList");
			if (zkSession.getAttribute("parameters") != null)
				zkSession.removeAttribute("parameters");
			if (zkSession.getAttribute("reportPath") != null)
				zkSession.removeAttribute("reportPath");
			if (zkSession.getAttribute("reportPath2") != null)
				zkSession.removeAttribute("reportPath2");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}