package org.wd.rfq.model;

import com.google.common.collect.Sets;
import org.json.JSONObject;
import org.wd.rfq.simulation.StandardNormalGenerator;

import java.security.InvalidParameterException;
import java.util.Set;
import java.util.logging.Logger;

public class ModelFactory {
    private static final String VASICEK_MODEL = "Vasicek";
    private static final String GBM_MODEL = "GBM";
    private static final Set<String> stochasticModels = Sets.newHashSet(VASICEK_MODEL, GBM_MODEL);
    private static final Logger LOGGER = Logger.getGlobal();

    public static Model getModel(JSONObject jsonModelRepresentation) {
        String modelName = getModelType(jsonModelRepresentation);

        if (stochasticModels.contains(modelName)) {
            return getStochasticModel(jsonModelRepresentation);
        }
        else {
            LOGGER.severe("Model type not implemented");
            throw new InvalidParameterException();
        }
    }

    private static StochasticModel getStochasticModel(JSONObject jsonModelRepresentation) {
        String modelName = getModelType(jsonModelRepresentation);
        long initialTimestamp = System.currentTimeMillis();
        double initialPrice = jsonModelRepresentation.getDouble("initialValue");
        long seed = jsonModelRepresentation.getLong("seed");

        if (modelName.equals(VASICEK_MODEL)) {
            double annualisedVol = jsonModelRepresentation.getDouble("annualisedVol");
            double longTermMean = jsonModelRepresentation.getDouble("longTermMean");
            double meanReversionSpeed = jsonModelRepresentation.getDouble("meanReversionSpeed");
            StandardNormalGenerator stdNormalGenerator = new StandardNormalGenerator(seed);
            return new VasicekModel(initialTimestamp, initialPrice, annualisedVol, longTermMean, meanReversionSpeed, stdNormalGenerator);
        }
        else if (modelName.equals(GBM_MODEL)) {
            double annualisedVol = jsonModelRepresentation.getDouble("annualisedVol");
            double annualisedMeanDrift = jsonModelRepresentation.getDouble("annualisedMeanDrift");
            StandardNormalGenerator stdNormalGenertor = new StandardNormalGenerator(seed);
            return new GbmModel(initialTimestamp, initialPrice, annualisedVol, annualisedMeanDrift, stdNormalGenertor);
        }
        else {
            LOGGER.severe("Model type not implemented");
            throw new InvalidParameterException();
        }
    }

    private static String getModelType(JSONObject jsonModelRep) {
        return jsonModelRep.getString("modelType");
    }
}
