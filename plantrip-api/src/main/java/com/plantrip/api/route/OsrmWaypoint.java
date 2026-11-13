package com.plantrip.api.route;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OsrmWaypoint(
        @JsonProperty("waypoint_index")
        Integer waypointIndex
) {
}
