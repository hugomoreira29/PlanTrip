package com.plantrip.api.route;


import java.util.List;

public record OsrmResponse(
        List<OsrmWaypoint> waypoints
) {
}
