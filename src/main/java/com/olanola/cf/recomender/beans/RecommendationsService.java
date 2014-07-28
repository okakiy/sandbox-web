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

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<RecommendedItem> recommendations = mahoutBean.getRecommender().recommend(userId, 55);
        System.out.println("printRecs: " + name);
        HashMap<String, Object> map = new HashMap<>();
        map.put("recommendations", recommendations);

        //printCommonalities
        long[] users = mahoutBean.getNeighborhood().getUserNeighborhood(userId);
        Map<String,Object> pairs = new HashMap<>();

        for (Long neighbor : users) {
            pairs.put(neighbor.toString(), TasteUtils.getCommonalities(mahoutBean.getDataModel(), userId, neighbor));
        }
        map.put("pairs", pairs);





        return TasteUtils.jsonRecs( map );
    }

}
