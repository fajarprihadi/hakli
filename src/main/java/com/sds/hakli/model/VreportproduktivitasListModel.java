package com.sds.hakli.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import com.sds.hakli.dao.Tp2kbDAO;
import com.sds.hakli.domain.Vreportproduktivitas;

public class VreportproduktivitasListModel extends AbstractPagingListModel<Vreportproduktivitas> implements Sortable<Vreportproduktivitas> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int _size = -1;
	List<Vreportproduktivitas> oList;  

	public VreportproduktivitasListModel(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}
	
	@Override
	protected List<Vreportproduktivitas> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {		
		Tp2kbDAO oDao = new Tp2kbDAO();		
		try {
			oList = oDao.reportProduktivitasPaging(itemStartNumber, pageSize, filter, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	@Override
	public int getTotalSize(String filter) {
		Tp2kbDAO oDao = new Tp2kbDAO();	
		try {
			_size = oDao.reportProduktivitasPageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}

	@Override
	public void sort(Comparator<Vreportproduktivitas> cmpr, boolean ascending) {		
		Collections.sort(oList, cmpr);
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);	
		
	}

	@Override
	public String getSortDirection(Comparator<Vreportproduktivitas> cmpr) {
		return null;
	}
}