package org.wd.rfq.model;

import com.google.common.collect.Sets;
import org.json.JSONObject;
import org.omg.CORBA.NO_IMPLEMENT;
import org.wd.rfq.util.RandomGenerator;
import org.wd.rfq.util.StandardNormalGenerator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Set;
import java.util.logging.Logger;

public class ModelFactory {
    private static final Set<String> stochasticModels = Sets.newHashSet("Vasicek");
    private static final Logger LOGGER = Logger.getGlobal();

    public static Model getModel(JSONObject jsonModelRepresentation) {
        String modelName = getModelType(jsonModelRepresentation);

        if (stochasticModels.contains(modelName)) {
            return getStochasticModel(jsonModelRepresentation);
        }
        else {
            LOGGER.severe("Model type not implemented");
            throw new NotImplementedException();
        }
    }

    private static StochasticModel getStochasticModel(JSONObject jsonModelRepresentation) {
        String modelName = getModelType(jsonModelRepresentation);

        if (modelName.equals("Vasicek")) {
            long initialTimestamp = System.currentTimeMillis();
            double initialPrice = jsonModelRepresentation.getDouble("initialValue");
            double annualisedVol = jsonModelRepresentation.getDouble("annualisedVol");
            double longTermMean = jsonModelRepresentation.getDouble("longTermMean");
            double meanReversionSpeed = jsonModelRepresentation.getDouble("meanReversionSpeed");
            long seed = jsonModelRepresentation.getLong("seed");

            StandardNormalGenerator stdNormalGenerator = new StandardNormalGenerator(seed);

            return new VasicekModel(initialTimestamp, initialPrice, annualisedVol, longTermMean, meanReversionSpeed, stdNormalGenerator);
        }
        else {
            LOGGER.severe("Model type not implemented");
            throw new NotImplementedException();
        }
    }

    private static String getModelType(JSONObject jsonModelRep) {
        return jsonModelRep.getString("modelType");
    }
}
