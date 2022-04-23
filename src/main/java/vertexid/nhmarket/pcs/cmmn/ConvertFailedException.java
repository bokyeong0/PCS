package vertexid.nhmarket.pcs.cmmn;

/**
 * 객체화에 실패한 경우 
 * 
 * @since 2016-01-28
 * @author Gracefulife
 */

public class ConvertFailedException extends IllegalArgumentException {
	private static final long serialVersionUID = 1L;

	public ConvertFailedException() {
		super("객체화에 실패하였습니다.");
	}

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}