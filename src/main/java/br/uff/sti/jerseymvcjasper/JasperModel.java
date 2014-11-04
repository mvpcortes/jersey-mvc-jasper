/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author marcos
 */
public class JasperModel<T> {

    private Map<String, Object> parameters;
    private List<T> listModels;

    private JasperModel() {
        parameters = new HashMap<>();
        listModels = new ArrayList<>();
    }

    public JasperModel(List<T> listModels, Map<String, Object> parameters) {
        this.parameters = parameters;
        this.listModels = listModels;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public List<T> getListModels() {
        return listModels;
    }

    public static JasperModel novo() {
        return new JasperModel();
    }

    public JasperModel models(T... arrayT) {
        listModels = new ArrayList(arrayT.length);
        for (T t : arrayT) {
            listModels.add(t);
        }
        return this;
    }

    public JasperModel putParam(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
}
