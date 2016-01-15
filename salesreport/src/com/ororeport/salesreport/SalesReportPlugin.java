package com.ororeport.salesreport;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.floreantpos.extension.ReportPlugin;
import com.floreantpos.model.CreditCardTransaction;
import com.floreantpos.model.DebitCardTransaction;
import com.floreantpos.model.DrawerPullReport;
import com.floreantpos.model.GiftCertificateTransaction;
import com.floreantpos.model.Gratuity;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.GenericDAO;
import com.floreantpos.report.SalesDetailedReport;
import com.floreantpos.report.SalesDetailedReport.DrawerPullData;
import com.ororeport.salesreport.action.SalesDetailReportAction;

@PluginImplementation
public class SalesReportPlugin implements ReportPlugin {

	@Override
	public AbstractAction[] getReportActions() {
		return new AbstractAction[] { 
				new SalesDetailReportAction()};
	}


	@Override
	public String getBaseMenuName() {
		return "Sales";
	}
	
	public SalesDetailedReport getSalesDetailedReport(Date fromDate, Date toDate) {
		GenericDAO dao = new GenericDAO();
		SalesDetailedReport report = new SalesDetailedReport();
		Session session = null;
		
		report.setFromDate(fromDate);
		report.setToDate(toDate);
		report.setReportTime(new Date());
		try {
			
			session = dao.getSession();
			
			Criteria criteria = session.createCriteria(DrawerPullReport.class);
			criteria.add(Restrictions.ge(DrawerPullReport.PROP_REPORT_TIME, fromDate));
			criteria.add(Restrictions.le(DrawerPullReport.PROP_REPORT_TIME, toDate));
			List list = criteria.list();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				DrawerPullReport drawerPullReport = (DrawerPullReport) iter.next();
				DrawerPullData data = new DrawerPullData();
				data.setDrawerPullId(drawerPullReport.getId());
				data.setTicketCount(drawerPullReport.getTicketCount());
				data.setIdealAmount(drawerPullReport.getDrawerAccountable());
				data.setActualAmount(drawerPullReport.getCashToDeposit());
				data.setVarinceAmount(drawerPullReport.getDrawerAccountable() - drawerPullReport.getCashToDeposit());
				report.addDrawerPullData(data);
			}
			
			criteria = session.createCriteria(CreditCardTransaction.class);
			criteria.add(Restrictions.ge(CreditCardTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(CreditCardTransaction.PROP_TRANSACTION_TIME, toDate));
			list = criteria.list();
			
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				CreditCardTransaction t = (CreditCardTransaction) iter.next();
				report.addCreditCardData(t);
			}
			
			criteria = session.createCriteria(DebitCardTransaction.class);
			criteria.add(Restrictions.ge(DebitCardTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(DebitCardTransaction.PROP_TRANSACTION_TIME, toDate));
			list = criteria.list();
			
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				DebitCardTransaction t = (DebitCardTransaction) iter.next();
				report.addCreditCardData(t);
			}
			
			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
			ProjectionList projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			Object[] object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setGiftCertReturnCount(((Number)object[0]).intValue());
			}
			if(object != null && object.length > 1 && object[1] instanceof Number) {
				report.setGiftCertReturnAmount(((Number)object[1]).doubleValue());
			}
			
			criteria = session.createCriteria(GiftCertificateTransaction.class);
			criteria.add(Restrictions.ge(GiftCertificateTransaction.PROP_TRANSACTION_TIME, fromDate));
			criteria.add(Restrictions.le(GiftCertificateTransaction.PROP_TRANSACTION_TIME, toDate));
			criteria.add(Restrictions.gt(GiftCertificateTransaction.PROP_GIFT_CERT_CASH_BACK_AMOUNT, Double.valueOf(0)));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum(GiftCertificateTransaction.PROP_GIFT_CERT_CASH_BACK_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setGiftCertChangeCount(((Number)object[0]).intValue());
			}
			if(object != null && object.length > 1 && object[1] instanceof Number) {
				report.setGiftCertChangeAmount(((Number)object[1]).doubleValue());
			}
			
			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g");
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_AMOUNT, Double.valueOf(0)));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.rowCount());
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setTipsCount(((Number)object[0]).intValue());
			}
			if(object != null && object.length > 1 && object[1] instanceof Number) {
				report.setChargedTips(((Number)object[1]).doubleValue());
			}
			
			criteria = session.createCriteria(Ticket.class);
			criteria.createAlias(Ticket.PROP_GRATUITY, "g");
			criteria.add(Restrictions.ge(Ticket.PROP_CREATE_DATE, fromDate));
			criteria.add(Restrictions.le(Ticket.PROP_CREATE_DATE, toDate));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_AMOUNT, Double.valueOf(0)));
			criteria.add(Restrictions.gt("g." + Gratuity.PROP_PAID, Boolean.TRUE));
			projectionList = Projections.projectionList();
			projectionList.add(Projections.sum("g." + Gratuity.PROP_AMOUNT));
			criteria.setProjection(projectionList);
			object = (Object[]) criteria.uniqueResult();
			if(object != null && object.length > 0 && object[0] instanceof Number) {
				report.setTipsPaid(((Number)object[0]).doubleValue());
			}
			
			return report;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
	
}
