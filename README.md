# JP Morgan ATS Coding Test
JP Morgan algorithmic trading strategist role coding test.

---

## Description

Write a programme in Java that simulates the process of replying to rfqs from clients for a 10y swap.

The application should, for example, construct a ticking 10y swap mid price and order book (for example 
from a random process generator – nice to have would be to simulate bund and asset swap spread prices 
separately and then combine together to construct a 10y price), simulate rfqs arriving at random intervals 
with size and direction and reply back with the corresponding price. If possible, implement the pricing 
and the rfq processes on 2 separate threads of the program. You should also log the working of the 
application (pricing, rfq events and prices, etc) to a log file (or simply print them to the console). 
Finally, try to build the program as a jar that can be run from the command line.

## Solution

My solution to this problem is a Java program modelling the Bund bond yield and the asset swap spread as separate
mid-rate processes (currently a Vasicek model or a Geometric Brownian motion model are supported). The bond yield
and swap spread models run in two separate ***pricing*** threads which update a central ***data manager*** which
can be queried for a swap mid-rate. Two other threads: an ***RFQ request thread*** and an ***RFQ response thread*** 
produce ***RFQ events*** which are submitted/consumed from an RFQ event queue. The RFQ requests are produced at
random intervals dictated by a max RFQ request rate and at a random notional (in millions) dictated by the max RFQ 
notional. A spread is then applied to the mid-rate dependent on the size of the RFQ, ranging between a hardcoded minimum
and maximum spread (1bps-10bps). All pricing updates and RFQ requests/responses are logged to the specified locations
(console and/or log file).

### Building the code

To build the code from source, ensure you have Maven installed on your machine. Run 

` mvn clean install`

from within the top level folder ***jpm_ats***. This will build the required jar file
***jpm_ats-1.0-SNAPSHOT-jar-with-dependencies.jar***. There is a small set of unit-tests
included which will be executed when running the above command.

### Running the code

The code expects 3 flags:

```
    -c (--config): Mandatory flag followed by the absolute path to a config file (e.g. C:\config\config.json
    -e (--enableConsoleLog): Flag to stream logger results to std out.
    -l (--logPath): Flag to stream results to a log file, followed by absolute file path.
```

The log file must take the following form:

```json
{
  "swapSpreadRefreshRate": 2,
  "bondYieldRefreshRate": 3,
  "maxRfqRefreshRate": 10,
  "rfqRequestRandomTimeSeed": 0,
  "maxRfqNotional": 100,
  "rfqRequestRandomNotionalSeed": 1,
  "swapSpreadModel": {
    "modelType": "Vasicek",
    "initialValue": 0.008,
    "annualisedVol": 0.5,
    "longTermMean": 0.01,
    "meanReversionSpeed": 0.5,
    "seed": 2
  },
  "bondYieldModel": {
    "modelType": "GBM",
    "initialValue": 0.012,
    "annualisedVol": 0.7,
    "annualisedMeanDrift": 0.02,
    "seed": 3
  }
}
```

All parameters above are mandatory and represent the following:

* ```swapSpreadRefreshRate```: A positive integer. The rate (in seconds) at which the swap spread simulator produces new swap spread ticks.
* ```bondYieldRefreshRate```: A positive integer. The rate (in seconds) at which the bond yield simulator produces new bond yield ticks.
* ```maxRfqRefreshRate```: A positive integer. RFQs are submitted at random using a uniform number generator. This value represents the max 
rate (in seconds) at which RFQs are submitted.
* ```rfqRequestRandomTimeSeed```: A positive integer. The seed for the max RFQ refresh rate unform random number generator.
* ```maxRfqNotional```: A positive integer. RFQs are submitted at a random size dictated by a uniform number generator. This value 
represents the max size (in millions) at which RFQs are submitted.
* ```rfqRequestRandomNotionalSeed```: A positive integer. The seed for the max RFQ notional request uniform
random number generator.
* ```swapSpreadModel```/```bondYieldModel```: A stochastic model for simulating the two price processes. Currently 
a Vasicek or GBM (Geometric Brownian Motion) model are supported.

##### Vasicek model
See https://en.wikipedia.org/wiki/Vasicek_model.
* ```modelType```: 'Vasicek'
* ```initialValue```: Positive double representing the initial rate/price for the Vasicek process.
* ```annualisedVol```: Positive double representing the annualised volatility for the Vasicek process.
* ```longTermMean```: Double representing the long term annualised mean for the Vasicek process.
* ```meanReversionSpeed```: Positive double representing the annualised mean reversion speed for the Vasicek process.
* ```seed```: Positive integer representing the seed for the driving standard normal process for the Vasicek process.

##### Geometric Brownian motion model
See https://en.wikipedia.org/wiki/Geometric_Brownian_motion.
* ```modelType```: 'GBM'
* ```initialValue```: Positive double representing the initial rate/price for the Vasicek process.
* ```annualisedVol```: Positive double representing the annualised volatility for the Vasicek process.
* ````annualisedMeanDrift````: Positive double representing the annualised mean drift for the Vasicek process.
* ```seed```: Positive integer representing the seed for the driving standard normal process for the Vasicek process.

---

Once you have configured your config file, decided on log destinations and have acquired the required
JAR file, the program can be run as follows:

```java -jar jpm_ats-1.0-SNAPSHOT-jar-with-dependencies.jar -c "C:/jpm_ats/config.json" -l "C:/jpm_ats/simulation.log" -e```

Sample config files are contained in:

```jpm_ats/src/main/config```