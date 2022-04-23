package vertexid.nhmarket.pcs.cmmn;

/**
 * 반복적으로 익셉션이 생기며 스택 트레이스를 관리할 필요가 없을때 사용
 * 
 * @since 2016-01-28
 * @author Gracefulife
 */
public class NotHaveStacktraceExeption extends RuntimeException {
	private static final long serialVersionUID = 1L;

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}
}