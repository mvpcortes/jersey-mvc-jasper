/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.sti.jerseymvcjasper;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import org.testng.annotations.Test;

/**
 *
 * @author marcos
 */
public class JasperModelTest {
    
    @Test
    public void when_call_putParam_then_param_is_added_in_model(){
        
        JasperModel<String> jm = JasperModel.create(String.class).putParam("X", "x");
        
        assertEquals(jm.getParameters().size(), 1);
        assertEquals(jm.getParam("X"), "x");
    }
    
    @Test
    public void when_call_add_models_then_all_itens_in_the_jasper_model(){
        String[] vecModels = new String[]{
            "a",
            "b",
            "c"
        };
        
        JasperModel<String> jm = JasperModel.create(String.class).addModels(vecModels);
        
        assertEquals(jm.getListModels().size(), 3);
        assertTrue(jm.getListModels().contains("a"));
        assertTrue(jm.getListModels().contains("b"));
        assertTrue(jm.getListModels().contains("c"));
    }
}
