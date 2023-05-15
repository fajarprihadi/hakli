package com.sds.hakli.viewmodel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.chart.AxisLabels;
import org.zkoss.chart.Charts;
import org.zkoss.chart.ChartsEvent;
import org.zkoss.chart.Color;
import org.zkoss.chart.Legend;
import org.zkoss.chart.Point;
import org.zkoss.chart.Series;
import org.zkoss.chart.Tooltip;
import org.zkoss.chart.XAxis;
import org.zkoss.chart.YAxis;
import org.zkoss.chart.model.CategoryModel;
import org.zkoss.chart.model.DefaultCategoryModel;
import org.zkoss.chart.model.DefaultXYModel;
import org.zkoss.chart.model.XYModel;
import org.zkoss.chart.plotOptions.PiePlotOptions;
import org.zkoss.chart.plotOptions.SeriesPlotOptions;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;

import com.sds.hakli.dao.TinvoiceDAO;
import com.sds.hakli.domain.Vpaymentbranch;
import com.sds.hakli.domain.Vpaymentmon;
import com.sds.utils.AppData;
import com.sds.utils.TimeUtil;

public class DashboardPaymentMonitorVm {

	private TinvoiceDAO oDao = new TinvoiceDAO();

	private List<Vpaymentmon> objList = new ArrayList<>();
	
	private Date begindate;
	private Date enddate;
	private String filter;
	
	private SimpleDateFormat datelocalFormatter = new SimpleDateFormat("yyyy-MM-dd");
	
	@Wire
    private Popup anchor;
    @Wire
    private Window msgBox;
    @Wire
	private Charts chart;
	@Wire
	private Window winPaymentmon;

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		Selectors.wireComponents(view, this, false);
		
        chart.addEventListener("onPlotClick", new EventListener<ChartsEvent>() {

			@Override
			public void onEvent(ChartsEvent event) throws Exception {
				anchor.open(chart, "at_pointer");
		        Point point = event.getPoint();
		        msgBox.setTitle(event.getSeries().getName());
		        // Locate the window's position by popup.
		        msgBox.setTop(anchor.getTop());
		        msgBox.setLeft(anchor.getLeft());
		        Label msg = (Label) msgBox.getFellow("msg");
		        String formattedDate = TimeUtil.getFormattedTime((Long) event.getCategory(),
		                "EEEEEEEEE, MMM dd, yyyy");
		        msg.setValue(formattedDate + ":\n Rp. " + NumberFormat.getInstance().format(point.getY()));
		        
		        if (msgBox.getLastChild() instanceof Grid)
		        	msgBox.removeChild(msgBox.getLastChild());
		        Grid grid = new Grid();
		        Columns columns = new Columns();
		        Column col1 = new Column("Provinsi");
		        Column col2 = new Column("Cabang");
		        Column col3 = new Column("Jumlah");
		        col3.setAlign("right");
		        columns.appendChild(col1);
		        columns.appendChild(col2);
		        columns.appendChild(col3);
		        grid.appendChild(columns);
		        Rows rows = new Rows();
		        
		        String date = TimeUtil.getFormattedTime((Long) event.getCategory(),
		                "yyyy-MM-dd");
		        for (Vpaymentbranch obj: oDao.listPaymentBranch(date)) {
		        	Row row = new Row();
		        	row.appendChild(new Label(obj.getProvname()));
		        	row.appendChild(new Label(obj.getCabang()));
		        	row.appendChild(new Label(NumberFormat.getInstance().format(obj.getPaidamount())));
		        	rows.appendChild(row);
		        }
		        grid.appendChild(rows);
		        msgBox.appendChild(grid);
		        msgBox.setVisible(true);
			}
		});
		
		msgBox.addEventListener("onClose", new EventListener<Event>() {

			@Override
			public void onEvent(Event event) throws Exception {
				 msgBox.setVisible(false);
				 event.stopPropagation();
			}
		});
		
		doChart();
	}
	
	public void doDefaultPeriod() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		begindate = cal.getTime();
		enddate = new Date();
	}

	@Command
	public void doChart() {
		try {
			if (begindate == null || enddate == null) {
				doDefaultPeriod();
			}
			filter = "date(paidtime) between '" + datelocalFormatter.format(begindate) + "' and '" + datelocalFormatter.format(enddate) + "'";
			objList = oDao.listPaymentMonitor(filter);
			XYModel model = new DefaultXYModel();
			for (Vpaymentmon obj: objList) {
				model.addValue(AppData.getInvoiceType(obj.getInvoicetype()), TimeUtil.parse(new SimpleDateFormat("MM-dd-yyyy").format(obj.getPaidtime())), obj.getPaidamount());
			}
			
			chart.setModel(model);

	        chart.getScrollablePlotArea().setMinWidth(700);

	        XAxis xAxis = chart.getXAxis();
	        xAxis.setType("datetime");
	        xAxis.setTickInterval(7 * 24 * 3600 * 1000); // one week
	        xAxis.setTickWidth(0);
	        xAxis.setGridLineWidth(1);
	        
	        AxisLabels xAxisLabels = xAxis.getLabels();
	        xAxisLabels.setAlign("left");
	        xAxisLabels.setX(3);
	        xAxisLabels.setY(-3);
	        
	        YAxis leftYAxis = chart.getYAxis();
	        leftYAxis.setTitle("");
	        AxisLabels leftYAxisLabels = leftYAxis.getLabels();
	        leftYAxisLabels.setAlign("left");
	        leftYAxisLabels.setX(3);
	        leftYAxisLabels.setY(16);
	        leftYAxisLabels.setFormat("{value:.,0f}");
	        leftYAxis.setShowFirstLabel(false);

	        YAxis rightYAxis = chart.getYAxis(1);
	        rightYAxis.setLinkedTo(0);
	        rightYAxis.setGridLineWidth(0);
	        rightYAxis.setOpposite(true);
	        rightYAxis.setTitle("");
	        AxisLabels rightYAxisLabels = rightYAxis.getLabels();
	        rightYAxisLabels.setAlign("right");
	        rightYAxisLabels.setX(-3);
	        rightYAxisLabels.setY(16);
	        rightYAxisLabels.setFormat("{value:.,0f}");
	        rightYAxis.setShowFirstLabel(false);
	        
	        Legend legend = chart.getLegend();
	        legend.setAlign("left");
	        legend.setVerticalAlign("top");
	        legend.setBorderWidth(0);

	        Tooltip tooltip = chart.getTooltip();
	        tooltip.setShared(true);
	        tooltip.setCrosshairs(true);

	        SeriesPlotOptions plotOptions = chart.getPlotOptions().getSeries();
	        plotOptions.setCursor("pointer");
	        plotOptions.setClassName("popup-on-click");
	        plotOptions.getMarker().setLineWidth(1);

	        Series series = chart.getSeries(0);
	        series.setLineWidth(4);
	        series.getMarker().setRadius(4);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Date getBegindate() {
		return begindate;
	}

	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	
}
