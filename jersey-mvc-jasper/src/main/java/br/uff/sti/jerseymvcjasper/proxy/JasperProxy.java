/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.sti.jerseymvcjasper.proxy;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

/**
 * Esta classe descreve uma interface de proxy para manipular os managers do jasper
 * @author marcos
 */
public interface JasperProxy {
    
    public JasperReport compileReport(InputStream inputStream) throws JRException;
    
    public JasperPrint fillReport(JasperReport jasperReport, Map mapParamerters, List listObjs)throws JRException;

    public void exportReportToPDFStream(JasperPrint jasperPrint, OutputStream outputStream)throws JRException;
    
}
