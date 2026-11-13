package com.plantrip.api.route;


public record OsrmResponse(
        List<OsrmWaypoint> waypoints
) {
}
