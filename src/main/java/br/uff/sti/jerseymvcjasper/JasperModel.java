/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.sti.jerseymvcjasper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the fields ($F{}) and parameters ($P{}) of a Jasper.
 * @author marcos
 * @param <T> The type of model in the JasperModel
 */
public class JasperModel<T> {

    private final Map<String, Object> parameters;
    private final List<T> listModels;

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

    public static <X> JasperModel<X> create(Class<X> clazz) {
        return new JasperModel<>();
    }

    public JasperModel addModels(T... arrayT) {
        listModels.clear();
        listModels.addAll(Arrays.asList(arrayT));
        return this;
    }

    public JasperModel putParam(String key, Object value) {
        parameters.put(key, value);
        return this;
    }

    Object getParam(String key) {
        return parameters.get(key);
    }
}
