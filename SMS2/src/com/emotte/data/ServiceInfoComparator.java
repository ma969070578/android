package com.emotte.data;

import java.util.Comparator;

public class ServiceInfoComparator implements Comparator<ServiceInfo> {

	@Override
	public int compare(ServiceInfo object1, ServiceInfo object2) {
		// TODO Auto-generated method stub

		return Double.compare(object2.getId(), object1.getId());
	}

}
