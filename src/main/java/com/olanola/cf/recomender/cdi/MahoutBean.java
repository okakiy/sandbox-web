package com.olanola.cf.recomender.cdi;

import com.olanola.cf.recomender.beans.TasteUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.annotation.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ManagedBean
@Singleton
@ApplicationScoped
@TransactionTimeout(value = 3600, unit = TimeUnit.SECONDS)

public class MahoutBean {
    private Recommender recommender;
    private FileDataModel dataModel;
    private UserNeighborhood neighborhood;
    private UserSimilarity userSimilarity;

    public Recommender getRecommender() {
        return recommender;
    }

    public FileDataModel getDataModel() {
        return dataModel;
    }

    public UserNeighborhood getNeighborhood() {
        return neighborhood;
    }

    public UserSimilarity getUserSimilarity() {
        return userSimilarity;
    }

    public static void showEnv () {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                    envName,
                    env.get(envName));
        }
    }

    public void initModel()  throws IOException, TasteException {
        System.out.println("==================== INJECTED AND CALLED! ====================");

        String homepath = System.getenv().get("HOME");
        if( homepath == null || homepath.isEmpty() ) {
            homepath = System.getenv().get("HOMEPATH");
            System.out.println("homepath=" + homepath);
        }

        dataModel = new FileDataModel(new File(homepath + File.separator + "tmp" + File.separator + "mahout_p2o.csv"));

        System.out.println("Data Model: Users: " + dataModel.getNumUsers() + " Items: " + dataModel.getNumItems());

        userSimilarity = new TanimotoCoefficientSimilarity(dataModel);

        neighborhood =
                new NearestNUserNeighborhood( 5, userSimilarity, dataModel);

        recommender =
                new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);

        List<RecommendedItem> recommendations =
                recommender.recommend(88364568, 5);

        System.out.println("Recommendations 88364568: \n" + TasteUtils.jsonRecs(recommendations));
    }
}
