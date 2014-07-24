/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.olanola.cf.recomender.beans;

import com.olanola.cf.recomender.cdi.MahoutBean;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.codehaus.jettison.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

/**
 * A simple CDI service which is able to say hello to someone
 *
 * @author Pete Muir
 *
 */
public class RecommendationsService {
    @Inject
    private MahoutBean mahoutBean;

    String toString(String name) throws TasteException {
        Long userId = Long.parseLong( name );
        List<RecommendedItem> recommendations = mahoutBean.getRecommender().recommend(userId, 5);
        System.out.println("printRecs: " + name);
        //TasteUtils.printRecs(recommendations,null);

        return TasteUtils.jsonRecs(recommendations);
    }

}
