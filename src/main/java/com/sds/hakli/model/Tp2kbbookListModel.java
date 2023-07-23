package com.sds.hakli.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.ext.Sortable;

import com.sds.hakli.dao.Tp2kbbookDAO;
import com.sds.hakli.domain.Tp2kbbook;

public class Tp2kbbookListModel extends AbstractPagingListModel<Tp2kbbook> implements Sortable<Tp2kbbook> {
			
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int _size = -1;
	List<Tp2kbbook> oList;  

	public Tp2kbbookListModel(int startPageNumber, int pageSize, String filter, String orderby) {
		super(startPageNumber, pageSize, filter, orderby);
	}
	
	@Override
	protected List<Tp2kbbook> getPageData(int itemStartNumber, int pageSize, String filter, String orderby) {		
		Tp2kbbookDAO oDao = new Tp2kbbookDAO();		
		try {
			oList = oDao.listPaging(itemStartNumber, pageSize, filter, orderby);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	@Override
	public int getTotalSize(String filter) {
		Tp2kbbookDAO oDao = new Tp2kbbookDAO();	
		try {
			_size = oDao.pageCount(filter);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return _size;
	}

	@Override
	public void sort(Comparator<Tp2kbbook> cmpr, boolean ascending) {		
		Collections.sort(oList, cmpr);
        fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);	
		
	}

	@Override
	public String getSortDirection(Comparator<Tp2kbbook> cmpr) {
		return null;
	}
}