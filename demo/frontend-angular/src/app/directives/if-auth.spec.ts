import { IfAuth } from './if-auth';

describe('IfAuth', () => {
  it('should create an instance', () => {
    const directive = new IfAuth();
    expect(directive).toBeTruthy();
  });
});
