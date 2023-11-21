package com.sds.hakli.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import com.sds.hakli.dao.TmutasiDAO;
import com.sds.hakli.domain.Tmutasi;

public class TmutasiListModel extends AbstractPagingListModel<Tmutasi> implements Sortable<Tmutasi> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int _size = -1;
	List<Tmutasi> oList;  

	public TmutasiListModel(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}
	
	@Override
	protected List<Tmutasi> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {		
		TmutasiDAO oDao = new TmutasiDAO();		
		try {
			oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	@Override
	public int getTotalSize(String filter) {
		TmutasiDAO oDao = new TmutasiDAO();	
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}

	@Override
	public void sort(Comparator<Tmutasi> cmpr, boolean ascending) {		
		Collections.sort(oList, cmpr);
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);	
		
	}

	@Override
	public String getSortDirection(Comparator<Tmutasi> cmpr) {
		return null;
	}
}