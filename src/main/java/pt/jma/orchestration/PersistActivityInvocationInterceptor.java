package pt.jma.orchestration;

public class PersistActivityInvocationInterceptor extends AbstractActivityInvocationInterceptor {

	@Override
	public void beforeInvoke(IRequest request) throws Exception {
		System.out.println("PersistActivityInvocationInterceptor beforeInvoke ");
	}

	@Override
	public void afterInvoke(IResponse response) throws Exception {
		response.setUUID(super.getActivity().getUUID());

		System.out.println("PersistActivityInvocationInterceptor afterInvoke ".concat(response.getUUID().toString()));

	}

	@Override
	public void handleException(Exception ex) throws Exception {
		throw ex;

	}

	@Override
	public void initTarget() throws Exception {
		// TODO Auto-generated method stub

	}

}
