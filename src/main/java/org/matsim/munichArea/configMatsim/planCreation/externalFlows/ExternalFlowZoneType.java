package org.matsim.munichArea.configMatsim.planCreation.externalFlows;

public enum ExternalFlowZoneType {

    NUTS3, BEZIRKE, BORDER;


    public static ExternalFlowZoneType getExternalFlowZoneTypeFromInt(int externalZoneTypeAsNumber){
        switch (externalZoneTypeAsNumber){
            case 2:
                return ExternalFlowZoneType.NUTS3;
            case 3:
                return ExternalFlowZoneType.BEZIRKE;
            case 9:
                return ExternalFlowZoneType.BORDER;
        }
    return null;
    }

}


