package com.grassnext.grassnextserver.gaussianplume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.grassnext.grassnextserver.common.Contours;
import com.grassnext.grassnextserver.common.MeasurementData;
import com.grassnext.grassnextserver.jni.GrassNextJni;
import com.grassnext.grassnextserver.util.Consts;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.grassnext.grassnextserver.gaussianplume.GaussianParametersService.jsonMapper;

/**
 * Controller for handling requests related to Gaussian Plume calculations.
 *
 */
@RestController
@RequestMapping("/api/gaussian-plume/")
@NoArgsConstructor
public class GaussianPlumeController {

    /**
     * Service instance for managing and providing Gaussian parameter data for plume dispersion calculations.
     *
     */
    GaussianParametersService gaussianParametersService;

    /**
     * Constructs a GaussianPlumeController and initializes the GaussianParametersService.
     *
     * @param gaussianParametersService the service responsible for managing and providing
     *                                  Gaussian parameter data for plume dispersion calculations
     */
    @Autowired
    GaussianPlumeController(
            GaussianParametersService gaussianParametersService
    ) {
        this.gaussianParametersService = gaussianParametersService;
    }

    /**
     * Handles the calculation of pollution contours based on the provided measurement data.
     *
     * @param measurementData the measurement data containing the necessary parameters
     *                        for pollution calculation, such as emissions and environmental data.
     * @return a response entity containing the computed pollution contours if the calculation
     *         is successful. Returns a bad request response with an error message if the
     *         measurement data is invalid, leads to an error during computation, or if the pollution
     *         level is below the measurable range.
     * @throws JsonProcessingException if there is an error during JSON parsing.
     *
     */
    @PostMapping(value = "measurement")
    @ResponseBody
    public ResponseEntity<Contours> calculatePollution(@RequestBody MeasurementData measurementData) throws JsonProcessingException {
        String jsonData = gaussianParametersService.getGaussianPlume(measurementData);

        if(jsonData.contains(Consts.INCORRECT_DATA_HEADER)) {
            return ResponseEntity.badRequest().body(new Contours(true, jsonData.substring(Consts.INCORRECT_DATA_HEADER.length()), -1, null));
        }

        String jsonContours = GrassNextJni.gnExecuteCount(jsonData);

        if (jsonContours == null) {
            return ResponseEntity.badRequest().body(new Contours(true, "Measurement error!", -1, null));
        } else {
            Contours c = jsonMapper.readValue(jsonContours, Contours.class);
            for(Contours.Contour contour : c.getContours() ) {
                if (contour.getPoints().isEmpty()) {
                    return ResponseEntity.badRequest().body(new Contours(true, "Pollution level below the measured range!", -1, null));
                }
            }

            return ResponseEntity.ok(jsonMapper.readValue(jsonContours, Contours.class));
        }
    }
}
